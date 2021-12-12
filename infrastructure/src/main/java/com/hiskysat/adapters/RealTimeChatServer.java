package com.hiskysat.adapters;

import com.hiskysat.data.ChatAddress;
import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.RealtimeChatServicePort;
import com.hiskysat.ports.spi.RealTimeChatServerPort;
import com.hiskysat.udp_server.Address;
import com.hiskysat.udp_server.UDPClient;
import com.hiskysat.udp_server.UDPServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RealTimeChatServer implements RealTimeChatServerPort {

    private final Map<String, UDPClient> clients;
    private final UDPServer udpServer;

    public RealTimeChatServer(ChatAddress address) throws SocketException {
        udpServer = new UDPServer(address.getPort());
        clients = new HashMap<>();
    }

    @Override
    public void listen(final RealtimeChatServicePort.ChatListener listener) {
        udpServer.start(new UDPServer.Listener() {
            @Override
            public void onMessageReceived(Address address, String content) {
                MessageDto message = MessageDto.builder()
                        .message(content)
                        .dateTime(new Date())
                        .build();
                listener.onMessageReceived(addressToChatAddress(address), message);
            }
        });
        new Thread(udpServer).start();

    }

    @Override
    public void stop() {
        udpServer.stop();
    }

    @Override
    public void connect(ChatAddress address) {
        try {
            addIfNotExistsClient(address);
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disconnect(ChatAddress address) {
        removeClient(address);
    }

    @Override
    public void send(ChatAddress chatAddress, String message) {
        try {
            UDPClient client = addIfNotExistsClient(chatAddress);
            client.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private UDPClient addIfNotExistsClient(ChatAddress address) throws UnknownHostException, SocketException {
        String id = getId(address);
        if (!clients.containsKey(id)) {
            Address udpAddress = chatAddressToAddress(address);
            clients.put(id, new UDPClient(udpAddress));
        }
        return clients.get(id);
    }

    private void removeClient(ChatAddress address) {
        clients.remove(getId(address));
    }

    private ChatAddress addressToChatAddress(Address address) {
        return ChatAddress.of(address.getAddress().getHostAddress(), address.getPort());
    }

    private Address chatAddressToAddress(ChatAddress chatAddress) throws UnknownHostException {
        return Address.of(InetAddress.getByName(chatAddress.getIpAddress()), chatAddress.getPort());
    }

    private String getId(ChatAddress address) {
        return address.getIpAddress() + ":" + address.getPort();
    }
}
