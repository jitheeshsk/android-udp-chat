package com.hiskysat.ports.spi;

import com.hiskysat.data.ChatDto;

import java.util.List;

public interface ChatPersistencePort {

    ChatDto addChat(Long userId, ChatDto chatDto);
    boolean deleteChatByUserId(Long userId);
    boolean deleteChatById(Long userId, Long chatId);
    ChatDto updateChat(Long userId, ChatDto chatDto);
    List<ChatDto> getChats(Long userId);

}
