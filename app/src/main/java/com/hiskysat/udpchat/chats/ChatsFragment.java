package com.hiskysat.udpchat.chats;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.databinding.FragmentChatsBinding;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;
import com.hiskysat.udpchat.service.ChatService;

import java.util.ArrayList;

import dagger.hilt.android.EntryPointAccessors;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

    private static final String ARG_USER_ID = "userId";

    public static Bundle createArgs(Long userId) {
        Bundle args = new Bundle();
        args.putLong(ARG_USER_ID, userId);
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LifecycleOwner owner = getViewLifecycleOwner();
        FragmentActivity activity = requireActivity();
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(activity.getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        ChatsViewModel viewModel = new ViewModelProvider(activity,
                entryPoint.viewModelFactory()).get(ChatsViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(owner);

        setHasOptionsMenu(true);

        long userId = ChatsFragmentArgs.fromBundle(getArguments()).getUserId();

        viewModel.getOpenMessageEvent().observe(owner, longEvent -> {
            Long chatId = longEvent.getContentIfNotHandled();
            if (chatId != null) {
                if (viewModel.isNewMessage(chatId)) {
                   ChatsFragmentDirections.ActionChatsFragmentToClientsFragment directions =
                           ChatsFragmentDirections.actionChatsFragmentToClientsFragment();
                    directions.setUserId(userId);
                    Navigation.findNavController(view).navigate(directions);
                } else {
                    ChatsFragmentDirections.ActionChatsFragmentToMessageFragment
                            action = ChatsFragmentDirections.actionChatsFragmentToMessageFragment();
                    action.setChatId(chatId);
                    Navigation.findNavController(view).navigate(action);
                }


            }
        });
        viewModel.start(userId);
        setupListAdapter(activity, viewModel);
        ChatService.startService(activity.getApplicationContext());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.chat_options, menu);
        menu.findItem(R.id.btn_view_profile).setOnMenuItemClickListener(menuItem -> {
            ChatsFragmentDirections.ActionChatsFragmentToCreateAccountFragment
                    action = ChatsFragmentDirections.actionChatsFragmentToCreateAccountFragment();
            action.setViewOnly(true);
            Navigation.findNavController(requireView()).navigate(action);
            return true;
        });
    }

    private void setupListAdapter(Context context, ChatsViewModel viewModel) {
        RecyclerView recyclerView =  binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
        ChatsAdapter chatsAdapter = new ChatsAdapter(viewModel);
        recyclerView.setAdapter(chatsAdapter);
    }
}
