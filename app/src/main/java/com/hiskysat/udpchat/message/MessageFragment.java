package com.hiskysat.udpchat.message;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.hiskysat.udpchat.ViewModelFactory;
import com.hiskysat.udpchat.databinding.FragmentMessageBinding;

public class MessageFragment extends Fragment {

    private MessageViewModel messageViewModel;
    private FragmentMessageBinding binding;

    public MessageFragment() {

    }

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        Long chatId = MessageFragmentArgs.fromBundle(getArguments()).getChatId();
        this.messageViewModel.start(chatId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = requireActivity();
        this.messageViewModel = new ViewModelProvider((ViewModelStoreOwner) activity,
                ViewModelFactory.getInstance(activity.getApplication())).get(MessageViewModel.class);
        binding.setViewmodel(messageViewModel);

    }
}
