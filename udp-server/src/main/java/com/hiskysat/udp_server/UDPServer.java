package com.hiskysat.udp_server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.crypto.Data;

public class UDPServer implements Runnable {

    private final static int BUFFER = 1024;
    private final static Charset CHARSET = StandardCharsets.UTF_8;

    private final DatagramSocket socket;
    private volatile boolean listen;
    private Listener listener;

    public UDPServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void start(Listener listener) {
        this.listen = true;
        this.listener = listener;
    }

    public void stop() {
        this.listen = false;
        this.listener = null;
    }

    @Override
    public void run() {
        byte[] buf = new byte[BUFFER];
        while (this.listen) {
            try {
                Arrays.fill(buf, (byte) 0);

                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String content = new String(buf, 0, buf.length, CHARSET);

                InetAddress clientAddress = packet.getAddress();
                int clientPort = packet.getPort();
                if (listener != null) {
                    listener.onMessageReceived(Address.of(clientAddress, clientPort), content);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public interface Listener {
        void onMessageReceived(Address address, String content);
    }

}