package com.hiskysat.udpchat.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Message;
import com.hiskysat.udpchat.databinding.RowMessageBinding;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> messages;
    private final MessageViewModel messageViewModel;

    public MessageAdapter(List<Message> messages, MessageViewModel messageViewModel) {
        this.messages = messages;
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
        holder.bind(messages.get(position));
    }

    @Override
    public long getItemId(int i) {
        return messages.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }


    private void setList(List<Message> messages) {
        this.messages = messages;
        notifyItemRangeChanged(0, messages.size());
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
