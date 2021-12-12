package com.hiskysat.udpchat.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationMessage {

    private Long clientId;
    private Long chatId;
    private String ipAddress;
    private String content;

}
