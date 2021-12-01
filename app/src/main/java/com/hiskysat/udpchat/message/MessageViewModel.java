package com.hiskysat.udpchat.message;

import android.app.Application;
import android.text.TextUtils;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.MessageServicePort;
import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.mapper.MessageMapper;

import java.util.ArrayList;
import java.util.List;


public class MessageViewModel extends ViewModel {

    private final MessageServicePort messageService;
    private Long chatId;

    public final ObservableList<Message> items = new ObservableArrayList<>();

    public final ObservableBoolean isSendButtonEnabled = new ObservableBoolean(false);
    public final ObservableBoolean dataLoading = new ObservableBoolean(false);
    public final ObservableBoolean empty = new ObservableBoolean(false);
    public final ObservableBoolean isDataLoadingError = new ObservableBoolean(false);

    public MessageViewModel(Application context, MessageServicePort messageService){
        this.messageService = messageService;

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.messageService.stopListening();
    }

    public void start(Long chatId) {
        this.chatId = chatId;
        this.messageService.startListening(new MessageServicePort.MessageEventCallback() {
            @Override
            public void onNewMessageReceived(Long chatId, MessageDto messageDto) {
                if (MessageViewModel.this.chatId.equals(chatId)) {
                    items.add(MessageMapper.messageFromMessageDto(messageDto));
                }
            }

            @Override
            public void onMessageUpdated(Long chatId, MessageDto messageDto) {
                if (MessageViewModel.this.chatId.equals(chatId)) {
                    Message message = MessageMapper.messageFromMessageDto(messageDto);
                    int index = items.indexOf(message);
                    if (index != -1){
                        items.set(index, message);
                    }
                }
            }
        });
        loadMessages();

    }

    public void onMessageChanged(String message) {
        this.isSendButtonEnabled.set(!TextUtils.isEmpty(message));
    }

    public void sendMessage(String message) {
        MessageDto messageDto = messageService.sendMessage(chatId, message);
        this.items.add(MessageMapper.messageFromMessageDto(messageDto));
    }

    public void loadMessages() {
        loadMessages(true);
    }

    private void loadMessages(boolean showLoadingUI) {
        if (showLoadingUI) {
            dataLoading.set(true);
        }
        messageService.getMessages(chatId, new MessageServicePort.LoadMessagesCallback() {
            @Override
            public void onMessagesLoaded(List<MessageDto> messages) {
                List<Message> messagesToShow = toMessages(messages);

                if (showLoadingUI) {
                    dataLoading.set(false);
                }

                isDataLoadingError.set(false);

                items.clear();
                items.addAll(messagesToShow);
                empty.set(items.isEmpty());
            }

            @Override
            public void onDataNotAvailable() {
                dataLoading.set(false);
                isDataLoadingError.set(true);
            }
        });

    }

    private List<Message> toMessages(List<MessageDto> messageDtos) {
        List<Message> messages = new ArrayList<>();
        for (MessageDto message: messageDtos) {
            messages.add(MessageMapper.messageFromMessageDto(message));
        }
        return messages;
    }

}
