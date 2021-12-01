package com.hiskysat.udpchat.message;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Message;

import java.util.List;

public class MessageListBindings {

    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Message> messages) {
        MessageAdapter adapter = (MessageAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.replaceData(messages);
        }
    }

}
