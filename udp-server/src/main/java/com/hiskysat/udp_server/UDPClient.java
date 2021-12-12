package com.hiskysat.udp_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPClient  {
    private final DatagramSocket socket;

    public UDPClient(Address address) throws SocketException {
        this.socket = new DatagramSocket(address.getPort(), address.getAddress());
    }

    public void sendMessage(String content) throws IOException {
        byte[] buf = content.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.send(packet);
    }

}
