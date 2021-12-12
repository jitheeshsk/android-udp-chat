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
import androidx.navigation.Navigation;

import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.databinding.FragmentCreateAccountBinding;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;
import com.hiskysat.udpchat.util.fragment.FragmentHelper;

import dagger.hilt.android.EntryPointAccessors;

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
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(activity.getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        viewModel = new ViewModelProvider(activity, entryPoint.viewModelFactory()).get(CreateAccountViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(activity);
        viewModel.getIsCreateAccountFormValidMediator().observe(activity, aBoolean -> binding.executePendingBindings());
        viewModel.getUserCreatedEvent().observe(activity, longEvent -> {
            Long userId = longEvent.getContentIfNotHandled();
            if (userId != null) {
                CreateAccountFragmentDirections.ActionCreateAccountFragmentToChatsFragment
                        directions = CreateAccountFragmentDirections.actionCreateAccountFragmentToChatsFragment();
                directions.setUserId(userId);
                Navigation.findNavController(view).navigate(directions);
            }

        });
        FragmentHelper.setTitle(this, isReadOnly() ? R.string.view_profile : R.string.create_account);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.start(isReadOnly());
        binding.executePendingBindings();
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.stop();
    }

    private boolean isReadOnly() {
        return CreateAccountFragmentArgs.fromBundle(getArguments()).getViewOnly();
    }
}
