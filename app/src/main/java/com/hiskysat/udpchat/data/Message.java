package com.hiskysat.udpchat.data;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private Long id;
    private boolean ownMessage;
    private String message;
    private String dateTime;
}
