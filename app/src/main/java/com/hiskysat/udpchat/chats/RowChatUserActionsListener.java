package com.hiskysat.udpchat.chats;

import com.hiskysat.udpchat.data.Chat;

public interface RowChatUserActionsListener {
    void onChatLongPressed(Chat chat);
    void onChatClicked(Chat chat);
}
