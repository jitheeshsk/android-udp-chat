package com.hiskysat.data;

import lombok.Data;

@Data
public class ChatDto {
    private Long id;
    private String name;
    private ChatAddress chatAddress;
    private MessageDto lastMessage;
}
