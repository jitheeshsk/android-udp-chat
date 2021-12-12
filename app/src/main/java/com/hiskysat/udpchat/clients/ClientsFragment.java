package com.hiskysat.udpchat.clients;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.chats.ChatsViewModel;
import com.hiskysat.udpchat.databinding.FragmentClientsBinding;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;

import java.util.ArrayList;

import dagger.hilt.android.EntryPointAccessors;

public class ClientsFragment extends Fragment {

    private FragmentClientsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentClientsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LifecycleOwner owner = getViewLifecycleOwner();
        FragmentActivity activity = requireActivity();
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(activity.getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        ClientsViewModel viewModel = new ViewModelProvider(activity,
                entryPoint.viewModelFactory()).get(ClientsViewModel.class);
        ChatsViewModel chatsViewModel = new ViewModelProvider(activity,
                entryPoint.viewModelFactory()).get(ChatsViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(owner);
        setupListAdapter(activity, viewModel);

        viewModel.getOpenAddClientEvent().observe(owner, longEvent -> {
            Long chatId = longEvent.getContentIfNotHandled();
            if (chatId != null) {
                NavDirections navDirections = ClientsFragmentDirections.actionClientsFragmentToAddClientFragment();
                Navigation.findNavController(view).navigate(navDirections);
            }
        });

        viewModel.getOpenMessageClientEvent().observe(owner, clientIdEvent -> {
            Long clientId = clientIdEvent.getContentIfNotHandled();
            if (clientId != null) {
                chatsViewModel.openMessageForClient(clientId);
                Navigation.findNavController(view).popBackStack();
            }
        });
        viewModel.start();
    }

    private void setupListAdapter(Context context, ClientsViewModel viewModel) {
        RecyclerView recyclerView =  binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        ClientsAdapter clientsAdapter = new ClientsAdapter(viewModel);
        recyclerView.setAdapter(clientsAdapter);
    }

}
