package com.hiskysat.service;

import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.MessageServicePort;

public class MessageService implements MessageServicePort {

    @Override
    public MessageDto addMessageDto(Long chatId, MessageDto message) {
        return null;
    }

    @Override
    public boolean deleteMessage(Long chatId, Long messageId) {
        return false;
    }

    @Override
    public MessageDto sendMessage(Long chatId, String message) {
        return null;
    }

    @Override
    public void getMessages(Long chatId, LoadMessagesCallback callback) {

    }

    @Override
    public void startListening(MessageEventCallback callback) {

    }

    @Override
    public void stopListening() {

    }

}


