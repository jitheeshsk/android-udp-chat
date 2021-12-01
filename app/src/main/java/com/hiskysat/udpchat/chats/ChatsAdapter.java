package com.hiskysat.udpchat.chats;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Chat;
import com.hiskysat.udpchat.databinding.RowChatBinding;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private List<Chat> chats;
    private final ChatsViewModel chatsViewModel;
    private final LifecycleOwner lifecycleOwner;


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowChatBinding binding;

        public ViewHolder(@NonNull RowChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat, LifecycleOwner owner) {
            this.binding.setChat(chat);
            this.binding.executePendingBindings();
            this.binding.setLifecycleOwner(owner);
        }
    }

    public ChatsAdapter(List<Chat> chats, ChatsViewModel viewModel, LifecycleOwner activity) {
        this.chats = chats;
        this.chatsViewModel = viewModel;
        this.lifecycleOwner = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowChatBinding binding = RowChatBinding.inflate(inflater, parent, false);
        binding.setListener(new RowChatUserActionsListener() {
            @Override
            public void onChatLongPressed(Chat chat) {
            }

            @Override
            public void onChatClicked(Chat chat) {
                chatsViewModel.openMessage(chat.getId());
            }
        });
        return new ChatsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(chats.get(position), lifecycleOwner);
    }

    @Override
    public long getItemId(int position) {
        return chats.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return chats != null ? chats.size() : 0;
    }

    public void replaceData(List<Chat> chats) {
        this.setList(chats);
    }

    private void setList(List<Chat> chats) {
        this.chats = chats;
        notifyItemRangeChanged(0, chats.size());
    }



}
