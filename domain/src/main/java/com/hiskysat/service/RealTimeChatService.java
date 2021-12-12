package com.hiskysat.service;

import com.hiskysat.data.ChatAddress;
import com.hiskysat.data.MessageDto;
import com.hiskysat.ports.api.RealtimeChatServicePort;

public class RealTimeChatService implements RealtimeChatServicePort {



    @Override
    public void listen(ChatAddress address, ChatListener listener) {

    }

    @Override
    public void send(ChatAddress address, MessageDto messageDto) {

    }
}
