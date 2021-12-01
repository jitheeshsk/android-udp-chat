package com.hiskysat.data;

import lombok.Data;

@Data
public class AccountDto {
    private Long id;
    private String ipAddress;
    private String password;
    private String userName;
}
