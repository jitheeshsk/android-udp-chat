package com.hiskysat.udpchat.mapper;

import com.hiskysat.data.MessageDto;
import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.util.AppDateTimeFormatter;

public class MessageMapper {

    private static final AppDateTimeFormatter FORMATTER = AppDateTimeFormatter.getInstance();

    public static Message messageFromMessageDto(MessageDto messageDto) {
        return Message.builder()
                .id(messageDto.getId())
                .message(messageDto.getMessage())
                .dateTime(FORMATTER.formatDate(messageDto.getDateTime()))
                .build();
    }

}
