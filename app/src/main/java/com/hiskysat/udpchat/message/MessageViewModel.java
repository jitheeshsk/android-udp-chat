package com.hiskysat.udpchat.message;

import android.text.TextUtils;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hiskysat.data.ChatAddress;
import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.ports.api.MessageServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.data.Placeholder;
import com.hiskysat.udpchat.mapper.MessageMapper;
import com.hiskysat.udpchat.util.rx.RxViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;


public class MessageViewModel extends RxViewModel {

    private static final long ID_NEW_ITEM = -1;

    private final MessageServicePort messageService;
    private final ChatServicePort chatServicePort;
    private final PublishSubject<String> eventSendMessage = PublishSubject.create();
    private final BehaviorSubject<Long> chatId = BehaviorSubject.create();

    private final MutableLiveData<String> title = new MutableLiveData<>();

    private final MutableLiveData<Placeholder> placeholder = new MutableLiveData<>(Placeholder.builder()
            .iconResId(R.drawable.ic_add)
            .labelResId(R.string.no_messages)
            .buttonLabelResId(R.string.no_messages_add)
            .showButton(true)
            .buttonClickListener(this::sendHelloMessage)
            .build());



    private final MutableLiveData<List<Message>> items = new MutableLiveData<>();

    private final MutableLiveData<String> message = new MutableLiveData<>();

    private final LiveData<Boolean> isSendButtonEnabled = Transformations.map(message, input ->
            !TextUtils.isEmpty(input));


    private final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>(false);
    private final LiveData<Boolean> empty = Transformations.map(items, (List::isEmpty));
    private final MutableLiveData<Boolean> isDataLoadingError = new MutableLiveData<>(false);
    private final MutableLiveData<Event<Integer>> messageSendEvent = new MutableLiveData<>();

    private final String firstMessage;

    public MessageViewModel(ChatServicePort chatServicePort, MessageServicePort messageService,
                            String firstMessage){
        this.messageService = messageService;
        this.chatServicePort = chatServicePort;
        this.firstMessage = firstMessage;
        initObservables();
    }


    public MutableLiveData<String> getMessage() {
        return message;
    }

    public LiveData<Boolean> getIsSendButtonEnabled() {
        return isSendButtonEnabled;
    }

    public MutableLiveData<Boolean> getDataLoading() {
        return dataLoading;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    public MutableLiveData<List<Message>> getItems() {
        return items;
    }

    public MutableLiveData<Placeholder> getPlaceholder() {
        return placeholder;
    }

    public LiveData<Event<Integer>> getMessageSendEvent() {
        return messageSendEvent;
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void initObservables() {
        Disposable sendChatSub = eventSendMessage
                .observeOn(Schedulers.io())
                .flatMapSingle(message -> chatServicePort.sendMessage(this.chatId.getValue(),
                        MessageDto.builder()
                                .message(message)
                                .status(MessageDto.MessageStatus.PENDING)
                                .build()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageDto -> {
                    if (items.getValue() != null) {
                        messageSendEvent.setValue(new Event<>(items.getValue().size()));
                    }
                });

        Disposable chatIdChange = chatId
                .distinctUntilChanged((aLong, aLong2) -> aLong.equals(aLong2) || aLong2.equals(ID_NEW_ITEM))
                .toFlowable(BackpressureStrategy.BUFFER)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> {
                    items.setValue(new ArrayList<>());
                    return aLong;
                })
                .observeOn(Schedulers.io())
                .switchMap(this::getChatLoadMessages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    dataLoading.setValue(true);
                    isDataLoadingError.setValue(false);
                })
                .doAfterNext(messageDtos -> dataLoading.setValue(false))
                .doOnError(throwable -> {
                    dataLoading.setValue(false);
                    isDataLoadingError.setValue(true);
                })
                .subscribe(chatAddressListPair -> {
                    ChatAddress address = chatAddressListPair.first;
                    if (!TextUtils.isEmpty(address.getIpAddress())) {
                        title.setValue(address.getIpAddress() + ":" + address.getPort());
                    } else {
                        title.setValue("");
                    }
                    items.setValue(chatAddressListPair.second);
                });

        addDisposable(chatIdChange);
        addDisposable(sendChatSub);
    }


    public void start(Long chatId) {
        this.chatId.onNext(chatId);
    }


    public void sendMessage(String message) {
        eventSendMessage.onNext(message);

    }


    private void sendHelloMessage() {
        sendMessage(firstMessage);
    }

    private Flowable<Pair<ChatAddress,List<Message>>> getChatLoadMessages(long chatId) {
        return loadMessages(chatId)
                .zipWith(getChatAddress(chatId).toFlowable(),
                        (messages, address) -> Pair.create(address, messages));
    }

    private Single<ChatAddress> getChatAddress(long chatId) {
        return chatServicePort.getChatAddress(chatId).switchIfEmpty(
                Single.just(ChatAddress.of("", 0)));
    }

    private Flowable<List<Message>> loadMessages(long chatId) {
       return messageService.getMessages(chatId)
                    .map(this::toMessages);
    }


    private List<Message> toMessages(List<MessageDto> messageDtos) {
        List<Message> messages = new ArrayList<>();
        for (MessageDto message: messageDtos) {
            messages.add(MessageMapper.messageFromMessageDto(message));
        }
        return messages;
    }

}
