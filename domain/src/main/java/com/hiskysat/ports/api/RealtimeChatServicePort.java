package com.hiskysat.ports.api;

import com.hiskysat.data.ChatAddress;
import com.hiskysat.data.MessageDto;

public interface RealtimeChatServicePort {

   void listen(ChatAddress address, ChatListener listener);
   void send(ChatAddress address, MessageDto messageDto);


   interface ChatListener {
       void onConnected();
       void onMessageReceived(ChatAddress address, MessageDto message);
       void onDisconnected();
   }

}
