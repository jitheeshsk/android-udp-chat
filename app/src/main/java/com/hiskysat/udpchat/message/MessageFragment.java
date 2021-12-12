package com.hiskysat.udpchat.message;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.clients.ClientsAdapter;
import com.hiskysat.udpchat.clients.ClientsViewModel;
import com.hiskysat.udpchat.databinding.FragmentMessageBinding;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;
import com.hiskysat.udpchat.util.fragment.FragmentHelper;

import java.util.ArrayList;

import dagger.hilt.android.EntryPointAccessors;

public class MessageFragment extends Fragment {

    private FragmentMessageBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = requireActivity();
        LifecycleOwner owner = getViewLifecycleOwner();
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(activity.getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        MessageViewModel viewModel = new ViewModelProvider(getViewModelStore(),
                entryPoint.viewModelFactory()).get(MessageViewModel.class);
        binding.setLifecycleOwner(owner);
        binding.setViewmodel(viewModel);

        viewModel.getMessageSendEvent().observe(owner, integerEvent -> {
            Integer position = integerEvent.getContentIfNotHandled();
            if (position != null) {
                binding.listMessages.smoothScrollToPosition(position);
            }
        });

        viewModel.getTitle().observe(owner, s -> FragmentHelper.setTitle(this, s));

        Long chatId = MessageFragmentArgs.fromBundle(getArguments()).getChatId();
        viewModel.start(chatId);
        setupListAdapter(activity, viewModel);
    }

    private void setupListAdapter(Context context, MessageViewModel viewModel) {
        RecyclerView recyclerView =  binding.listMessages;
        LinearLayoutManager manager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        MessageAdapter clientsAdapter = new MessageAdapter(
                viewModel
        );

        recyclerView.setAdapter(clientsAdapter);
    }

}
