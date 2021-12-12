package com.hiskysat.udpchat.mapper;

import com.hiskysat.data.ChatDto;
import com.hiskysat.data.MessageDto;
import com.hiskysat.udpchat.data.Chat;
import com.hiskysat.udpchat.util.AppDateTimeFormatter;

public class ChatMapper {

    private static final AppDateTimeFormatter FORMATTER = AppDateTimeFormatter.getInstance();

    public static Chat chatFromChatDto(ChatDto chatDto) {
        MessageDto messageDto = chatDto.getLastMessage();
        return Chat.builder()
                .id(chatDto.getId())
                .message(messageDto == null ? "" : messageDto.getMessage())
                .title(String.format("%s (%s)", chatDto.getName(), chatDto.getChatAddress().getIpAddress()))
                .dateTime(messageDto == null ? "" : FORMATTER.formatDate(messageDto.getDateTime()))
                .build();
    }

}
