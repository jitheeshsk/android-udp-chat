package com.hiskysat.udpchat.addclient;

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
import androidx.navigation.Navigation;

import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.clients.ClientsViewModel;
import com.hiskysat.udpchat.databinding.FragmentAddClientBinding;
import com.hiskysat.udpchat.di.ViewModelFactoryEntryPoint;
import com.hiskysat.udpchat.util.alert.AlertHelper;

import dagger.hilt.android.EntryPointAccessors;

public class AddClientFragment extends Fragment {

    private FragmentAddClientBinding binding;
    private AddClientViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddClientBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = requireActivity();
        LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
        ViewModelFactoryEntryPoint entryPoint = EntryPointAccessors.fromApplication(activity.getApplicationContext(),
                ViewModelFactoryEntryPoint.class);
        viewModel = new ViewModelProvider(getViewModelStore(), entryPoint.viewModelFactory()).get(AddClientViewModel.class);
        binding.setViewmodel(viewModel);
        binding.setLifecycleOwner(lifecycleOwner);

        ClientsViewModel clientsViewModel = new ViewModelProvider(activity, entryPoint.viewModelFactory())
                .get(ClientsViewModel.class);

        viewModel.getClientAddedEvent().observe(lifecycleOwner, addEvent -> {
            AddClientViewModel.ClientAddEvent event =
                    addEvent.getContentIfNotHandled();
            if (event != null) {
                String message = getString(R.string.msg_add_client_success, event.getName());
                AlertHelper.showToast(view, message);
                Navigation.findNavController(view).popBackStack();
                clientsViewModel.openMessageClient(event.getId());
            }

        });
        viewModel.getClientAddFailedEvent().observe(lifecycleOwner, failureEvent ->
                AlertHelper.showToast(view, R.string.msg_add_client_failed));
        viewModel.initSetup();
    }


    @Override
    public void onResume() {
        super.onResume();
        viewModel.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stop();
    }
}
