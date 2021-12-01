package com.hiskysat.udpchat.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Account {
    private String ipAddress;
    private int port;
    private String name;
    private String password;
    private String confirmPassword;

}
