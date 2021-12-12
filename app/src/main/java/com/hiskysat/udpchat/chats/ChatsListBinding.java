package com.hiskysat.udpchat.chats;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Chat;

import java.util.List;

public class ChatsListBinding {

    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Chat> chats) {
        ChatsAdapter adapter = (ChatsAdapter) recyclerView.getAdapter();
        if (adapter != null && chats != null) {
            adapter.replaceData(chats);
        }
    }
}
