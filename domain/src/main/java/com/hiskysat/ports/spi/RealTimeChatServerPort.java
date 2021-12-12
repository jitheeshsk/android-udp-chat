package com.hiskysat.ports.spi;

import com.hiskysat.data.ChatAddress;
import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.RealtimeChatServicePort;

public interface RealTimeChatServerPort {
    void listen(RealtimeChatServicePort.ChatListener listener);
    void stop();

    void connect(ChatAddress address);
    void disconnect(ChatAddress address);
    void send(ChatAddress address, String content);


    interface ChatListener {
        void onConnected();
        void onMessageReceived(ChatAddress address, MessageDto message);
        void onDisconnected();
    }
}
