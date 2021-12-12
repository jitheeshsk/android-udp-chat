package com.hiskysat.data;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageDto {
    private long id;
    private String message;
    private Date dateTime;
}
