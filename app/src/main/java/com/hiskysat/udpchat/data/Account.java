package com.hiskysat.udpchat.data;

public class Account {
    private final String ipAddress;
    private final int port;
    private final String name;
    private final String password;

    public Account(String ipAddress, int port, String name, String password) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.name = name;
        this.password = password;
    }
}
