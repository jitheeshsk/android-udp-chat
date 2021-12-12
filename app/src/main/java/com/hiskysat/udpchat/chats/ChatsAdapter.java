package com.hiskysat.udpchat.chats;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Chat;
import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.databinding.RowChatBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsAdapter extends ListAdapter<Chat, ChatsAdapter.ViewHolder> {

    private final ChatsViewModel chatsViewModel;

    private static final DiffUtil.ItemCallback<Chat> DIFF_CALLBACK = new DiffUtil.ItemCallback<Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
            return Objects.equals(oldItem.getMessage(), newItem.getMessage())
                    && Objects.equals(oldItem.getTitle(), newItem.getTitle())
                    && Objects.equals(oldItem.getDateTime(), newItem.getDateTime());
        }
    };



    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowChatBinding binding;

        public ViewHolder(@NonNull RowChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Chat chat) {
            this.binding.setChatRoom(chat);
            this.binding.executePendingBindings();
        }
    }

    public ChatsAdapter(ChatsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.chatsViewModel = viewModel;
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
        holder.bind(getItem(position));
    }


    public void replaceData(List<Chat> chats) {
        this.setList(chats);
    }

    private void setList(List<Chat> chats) {
        submitList(chats);
    }



}
