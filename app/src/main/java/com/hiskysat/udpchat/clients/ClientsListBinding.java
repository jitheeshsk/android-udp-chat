package com.hiskysat.udpchat.clients;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.data.Client;

import java.util.List;

public class ClientsListBinding {

    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<Client> clients) {
        ClientsAdapter adapter = (ClientsAdapter) recyclerView.getAdapter();
        if (adapter != null && clients != null) {
            adapter.replaceData(clients);
        }
    }

}
