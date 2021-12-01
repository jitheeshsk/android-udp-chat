package com.hiskysat.udpchat.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Chat {

    private Long id;
    private String title;
    private String message;
    private String dateTime;

}
