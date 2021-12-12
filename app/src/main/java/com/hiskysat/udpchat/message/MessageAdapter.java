package com.hiskysat.udpchat.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.databinding.RowMessageBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends ListAdapter<Message, MessageAdapter.ViewHolder> {

    private final MessageViewModel messageViewModel;

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK = new DiffUtil.ItemCallback<Message>() {
        @Override
        public boolean areItemsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Message oldItem, @NonNull Message newItem) {
            return Objects.equals(oldItem.getMessage(), newItem.getMessage())
                    && Objects.equals(oldItem.isOwnMessage(), newItem.isOwnMessage())
                    && Objects.equals(oldItem.getDateTime(), newItem.getDateTime());
        }
    };

    public MessageAdapter(MessageViewModel messageViewModel) {
        super(DIFF_CALLBACK);
        this.messageViewModel = messageViewModel;
    }

    public void replaceData(List<Message> messages) {
        setList(messages);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowMessageBinding binding = RowMessageBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    private void setList(List<Message> messages) {
        submitList(messages);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowMessageBinding binding;

        public ViewHolder(@NonNull RowMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Message message) {
            this.binding.setMessage(message);
            this.binding.executePendingBindings();
        }
    }
}
