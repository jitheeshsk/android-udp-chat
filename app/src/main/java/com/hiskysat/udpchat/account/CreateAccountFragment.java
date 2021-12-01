package com.hiskysat.udpchat.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.hiskysat.udpchat.ViewModelFactory;
import com.hiskysat.udpchat.databinding.FragmentCreateAccountBinding;

public class CreateAccountFragment extends Fragment {

    private FragmentCreateAccountBinding binding;
    private CreateAccountViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = requireActivity();
        viewModel = new ViewModelProvider(activity, ViewModelFactory.getInstance(activity.getApplication())).get(CreateAccountViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(activity);
        viewModel.getIsCreateAccountFormValidMediator().observe(activity, aBoolean -> {
            binding.executePendingBindings();
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stop();
    }
}
