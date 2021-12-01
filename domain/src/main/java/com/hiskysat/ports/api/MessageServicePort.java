package com.hiskysat.ports.api;

import com.hiskysat.data.MessageDto;

import java.util.List;


public interface MessageServicePort {
    MessageDto addMessageDto(Long chatId, MessageDto message);
    boolean deleteMessage(Long chatId, Long messageId);

    MessageDto sendMessage(Long chatId, String message);
    void getMessages(Long chatId, LoadMessagesCallback callback);
    void startListening(MessageEventCallback callback);
    void stopListening();

    interface LoadMessagesCallback {

        void onMessagesLoaded(List<MessageDto> messages);
        void onDataNotAvailable();

    }

    interface MessageEventCallback {

        void onNewMessageReceived(Long chatId, MessageDto messageDto);
        void onMessageUpdated(Long chatId, MessageDto messageDto);

    }


}
