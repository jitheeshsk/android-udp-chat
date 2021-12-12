package com.hiskysat.udpchat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.ports.api.ClientServicePort;
import com.hiskysat.ports.api.MessageServicePort;
import com.hiskysat.ports.spi.ChatInterfacePort;
import com.hiskysat.udpchat.account.CreateAccountViewModel;
import com.hiskysat.udpchat.addclient.AddClientViewModel;
import com.hiskysat.udpchat.chats.ChatsViewModel;
import com.hiskysat.udpchat.clients.ClientsViewModel;
import com.hiskysat.udpchat.message.MessageViewModel;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final MessageServicePort messageService;
    private final ChatServicePort chatService;
    private final AccountServicePort accountService;
    private final ClientServicePort clientService;
    private final ChatInterfacePort chatInterfacePort;
    private final Context context;

    @Inject
    public ViewModelFactory(@ApplicationContext Context context,
                            MessageServicePort messageService, ChatInterfacePort chatInterfacePort,
                            ChatServicePort chatService, AccountServicePort accountService,
                            ClientServicePort clientService) {
        this.context = context;
        this.messageService = messageService;
        this.chatService = chatService;
        this.accountService = accountService;
        this.clientService = clientService;
        this.chatInterfacePort = chatInterfacePort;
    }


    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MessageViewModel.class)) {
            return (T) new MessageViewModel(chatService, messageService,
                    context.getString(R.string.no_messages_first_message));
        } else if (modelClass.isAssignableFrom(ChatsViewModel.class)) {
            return (T) new ChatsViewModel(chatService);
        } else if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(accountService);
        } else if (modelClass.isAssignableFrom(CreateAccountViewModel.class)) {
            return (T) new CreateAccountViewModel(accountService);
        } else if (modelClass.isAssignableFrom(AddClientViewModel.class)) {
            return (T) new AddClientViewModel(clientService);
        } else if (modelClass.isAssignableFrom(ClientsViewModel.class)) {
            return (T) new ClientsViewModel(clientService);
        }
        throw new IllegalArgumentException("Unknown model class given: " + modelClass.getName());
    }
}
