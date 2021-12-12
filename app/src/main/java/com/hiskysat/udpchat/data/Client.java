package com.hiskysat.udpchat.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Client {

    private Long clientId;
    private String title;

}
