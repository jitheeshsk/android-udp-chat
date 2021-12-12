package com.hiskysat.service;

import com.hiskysat.data.ChatDto;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.ports.spi.ChatPersistencePort;

import java.util.List;

public class ChatService implements ChatServicePort {

    private ChatPersistencePort chatPersistencePort;

    @Override
    public ChatDto addChat(Long userId, ChatDto chatDto) {
        return chatPersistencePort.addChat(userId, chatDto);
    }

    @Override
    public boolean deleteChat(Long userId, Long id) {
        return chatPersistencePort.deleteChatById(userId, id);
    }

    @Override
    public void getChats(Long userId, LoadChatsCallback callback) {

    }

    @Override
    public void startListening(ChatEventsCallback callback) {

    }

    @Override
    public void stopListening() {

    }
}
