package com.hiskysat.udpchat.chats;

import android.app.Activity;
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
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.Navigation;

import com.hiskysat.udpchat.ViewModelFactory;
import com.hiskysat.udpchat.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {

    private FragmentChatsBinding binding;

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
        ChatsViewModel viewModel = new ViewModelProvider((ViewModelStoreOwner) activity,
                ViewModelFactory.getInstance(activity.getApplication())).get(ChatsViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(owner);

        viewModel.getOpenMessageEvent().observe(owner, longEvent -> {
            Long chatId = longEvent.getContentIfNotHandled();
            if (chatId != null) {
                ChatsFragmentDirections.ActionChatsFragmentToMessageFragment
                        action = ChatsFragmentDirections.actionChatsFragmentToMessageFragment();
                action.setChatId(chatId);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

}
