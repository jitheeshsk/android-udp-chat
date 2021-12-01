package com.hiskysat.udpchat.chats;

import android.app.Application;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.ChatDto;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Chat;
import com.hiskysat.udpchat.data.Placeholder;
import com.hiskysat.udpchat.mapper.ChatMapper;

import java.util.ArrayList;
import java.util.List;

public class ChatsViewModel extends ViewModel {

    private static final long NEW_ITEM_ID = -1;

    private final ChatServicePort chatServicePort;
    private long userId;

    private final MutableLiveData<Placeholder> placeholder = new MutableLiveData<>(Placeholder.builder()
            .iconResId(R.drawable.ic_add)
            .labelResId(R.string.no_chats)
            .buttonLabelResId(R.string.no_chats_add)
            .showButton(true)
            .buttonClickListener(this::openNewMessage)
            .build());

    private final MutableLiveData<List<Chat>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();
    public final LiveData<Boolean> empty = Transformations.map(items, (List::isEmpty));
    public final ObservableBoolean isDataLoadingError = new ObservableBoolean();

    private final MutableLiveData<Event<Long>> openMessageEvent = new MutableLiveData<>();


    public ChatsViewModel(Application context, ChatServicePort chatServicePort) {
        this.chatServicePort = chatServicePort;

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
        this.userId = userId;
        this.loadChats();
    }

    public void loadChats() {
        loadChats(true);
    }

    public void loadChats(boolean showLoadingUi) {
        if (showLoadingUi) {
            this.dataLoading.setValue(true);
        }
        this.chatServicePort.getChats(userId, new ChatServicePort.LoadChatsCallback() {
            @Override
            public void onChatsLoaded(List<ChatDto> chats) {

                if (showLoadingUi) {
                    dataLoading.setValue(false);
                }

                isDataLoadingError.set(false);

                items.setValue(toChats(chats));
            }

            @Override
            public void onDataNotAvailable() {
                dataLoading.setValue(false);
                isDataLoadingError.set(true);
            }
        });

    }

    public void openNewMessage() {
        openMessage(NEW_ITEM_ID);
    }

    public void openMessage(Long id) {
        openMessageEvent.setValue(new Event<>(id));
    }

    private List<Chat> toChats(List<ChatDto> chatDtos) {
        List<Chat> chats = new ArrayList<>();
        for (ChatDto chat: chatDtos) {
            chats.add(ChatMapper.chatFromChatDto(chat));
        }
        return chats;
    }

}
