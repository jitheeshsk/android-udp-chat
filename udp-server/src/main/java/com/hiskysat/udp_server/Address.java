package com.hiskysat.udp_server;

import java.net.InetAddress;

public class Address {
    private final InetAddress address;
    private final int port;

    public static Address of(InetAddress address, int port) {
        return new Address(address,port);
    }

    private Address(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }
}
