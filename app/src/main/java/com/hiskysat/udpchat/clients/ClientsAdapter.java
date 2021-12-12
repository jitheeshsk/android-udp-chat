package com.hiskysat.udpchat.clients;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Client;
import com.hiskysat.udpchat.databinding.RowClientBinding;

import java.util.List;

public class ClientsAdapter extends ListAdapter<Client, ClientsAdapter.ViewHolder> {

    private final ClientsViewModel clientsViewModel;

    private static final DiffUtil.ItemCallback<Client> DIFF_CALLBACK = new DiffUtil.ItemCallback<Client>() {
        @Override
        public boolean areItemsTheSame(@NonNull Client oldItem, @NonNull Client newItem) {
            return oldItem.getClientId().equals(newItem.getClientId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Client oldItem, @NonNull Client newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowClientBinding binding;

        public ViewHolder(@NonNull RowClientBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Client client) {
            this.binding.setClient(client);
            this.binding.executePendingBindings();
        }
    }

    public ClientsAdapter(ClientsViewModel viewModel) {
        super(DIFF_CALLBACK);
        this.clientsViewModel = viewModel;
    }

    @NonNull
    @Override
    public ClientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowClientBinding binding = RowClientBinding.inflate(inflater, parent, false);
        binding.setListener(client -> clientsViewModel.openMessageClient(client.getClientId()));
        return new ClientsAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientsAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }


    public void replaceData(List<Client> clients) {
        this.setList(clients);
    }

    private void setList(List<Client> clients) {
        submitList(clients);
    }



}
