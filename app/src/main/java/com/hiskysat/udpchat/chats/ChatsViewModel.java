package com.hiskysat.udpchat.chats;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hiskysat.data.ChatDto;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Chat;
import com.hiskysat.udpchat.data.Placeholder;
import com.hiskysat.udpchat.mapper.ChatMapper;
import com.hiskysat.udpchat.util.rx.RxViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import lombok.Builder;
import lombok.Data;

public class ChatsViewModel extends RxViewModel {

    private static final long NEW_ITEM_ID = -1;

    private final ChatServicePort chatServicePort;

    private final BehaviorSubject<Long> userId = BehaviorSubject.create();
    private final BehaviorSubject<Long> clientId = BehaviorSubject.create();

    private final MutableLiveData<Placeholder> placeholder = new MutableLiveData<>(Placeholder.builder()
            .iconResId(R.drawable.ic_add)
            .labelResId(R.string.no_chats)
            .buttonLabelResId(R.string.no_chats_add)
            .showButton(true)
            .buttonClickListener(this::openNewMessage)
            .build());

    private final MutableLiveData<List<Chat>> items = new MutableLiveData<>();
    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();
    private final LiveData<Boolean> empty = Transformations.map(items, (List::isEmpty));
    private final ObservableBoolean isDataLoadingError = new ObservableBoolean();

    private final MutableLiveData<Event<Long>> openMessageEvent = new MutableLiveData<>();


    public ChatsViewModel(ChatServicePort chatServicePort) {
        this.chatServicePort = chatServicePort;
        initObservables();

    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public MutableLiveData<List<Chat>> getItems() {
        return items;
    }

    public MutableLiveData<Placeholder> getPlaceholder() {
        return placeholder;
    }

    public MutableLiveData<Event<Long>> getOpenMessageEvent() {
        return openMessageEvent;
    }

    public void start(Long userId) {
        this.userId.onNext(userId);
    }


    public void openMessageForClient(long clientId) {
        this.clientId.onNext(clientId);
    }

    public boolean isNewMessage(long id) {
        return id == NEW_ITEM_ID;
    }

    public void openNewMessage() {
        openMessage(NEW_ITEM_ID);
    }

    public void openMessage(Long id) {
        openMessageEvent.setValue(new Event<>(id));
    }


    private void initObservables() {
        Disposable userIdChange = userId
                .distinctUntilChanged(Long::equals)
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(userId -> loadChats(userId, true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items::setValue);
        addDisposable(userIdChange);

        Disposable createChatSub = this.clientId
                .distinctUntilChanged((previous, current) -> previous.equals(current) || current == NEW_ITEM_ID)
                .observeOn(Schedulers.io())
                .flatMapSingle(clientId -> this.chatServicePort.createChatIfNotExists(this.userId.getValue(),
                        clientId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatDto -> {
                    openMessage(chatDto.getId());
                    clientId.onNext(NEW_ITEM_ID);
                });

        addDisposable(createChatSub);
    }



    private Flowable<List<Chat>> loadChats(long userId, boolean showLoadingUi) {
        return this.chatServicePort.getChats(userId)
                .map(this::toChats)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    isDataLoadingError.set(false);
                    if (showLoadingUi) {
                        dataLoading.setValue(true);
                    }
                })
                .doAfterNext(subscription -> dataLoading.setValue(false))
                .doOnError(error -> {
                    dataLoading.setValue(false);
                    isDataLoadingError.set(true);
                });

    }

    private List<Chat> toChats(List<ChatDto> chatDtos) {
        List<Chat> chats = new ArrayList<>();
        for (ChatDto chat: chatDtos) {
            chats.add(ChatMapper.chatFromChatDto(chat));
        }
        return chats;
    }

    @Builder
    @Data
    protected static class MessageEvent {
        Long userId;
        Long clientId;
        Long chatId;
    }

}
