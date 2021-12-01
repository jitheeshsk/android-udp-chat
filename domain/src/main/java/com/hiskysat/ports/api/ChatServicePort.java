package com.hiskysat.ports.api;

import com.hiskysat.data.ChatDto;
import com.hiskysat.data.MessageDto;

import java.util.List;

public interface ChatServicePort {

    ChatDto addChat(Long userId, ChatDto chatDto);
    boolean deleteChat(Long userId, Long id);
    void getChats(Long userId, LoadChatsCallback callback);
    void startListening(ChatEventsCallback callback);
    void stopListening();

    interface LoadChatsCallback {

        void onChatsLoaded(List<ChatDto> chats);
        void onDataNotAvailable();

    }

    interface ChatEventsCallback {

        void onNewChatReceived(Long userId, ChatDto chatDto);
        void onChatUpdated(Long userId, ChatDto chatDto);


    }



}
