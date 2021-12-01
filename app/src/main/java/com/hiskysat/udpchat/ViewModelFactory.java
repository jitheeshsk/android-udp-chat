package com.hiskysat.udpchat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hiskysat.AppDatabase;
import com.hiskysat.adapters.AccountRoomAdapter;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.ports.api.MessageServicePort;
import com.hiskysat.repository.AccountsDao;
import com.hiskysat.service.ChatService;
import com.hiskysat.service.MessageService;
import com.hiskysat.udpchat.account.CreateAccountViewModel;
import com.hiskysat.udpchat.chats.ChatsViewModel;
import com.hiskysat.udpchat.message.MessageViewModel;
import com.hiskysat.service.AccountService;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactory INSTANCE;


    private final Application application;
    private final MessageServicePort messageService;
    private final ChatServicePort chatService;
    private final AccountServicePort accountService;

    public static ViewModelFactory getInstance(Application application) {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(application,
                            new MessageService(), new ChatService(), getAccountService(application));
                }
            }
        }
        return INSTANCE;
    }

    private static AccountService getAccountService(Context context) {
        return new AccountService(new AccountRoomAdapter(context));
    }

    private ViewModelFactory(Application application, MessageServicePort messageService,
                             ChatServicePort chatService, AccountServicePort accountService) {
        this.application = application;
        this.messageService = messageService;
        this.chatService = chatService;
        this.accountService = accountService;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MessageViewModel.class)) {
            return (T) new MessageViewModel(application, messageService);
        } else if (modelClass.isAssignableFrom(ChatsViewModel.class)) {
            return (T) new ChatsViewModel(application, chatService);
        } else if (modelClass.isAssignableFrom(MainActivityViewModel.class)) {
            return (T) new MainActivityViewModel(application, accountService);
        } else if (modelClass.isAssignableFrom(CreateAccountViewModel.class)) {
            return (T) new CreateAccountViewModel(accountService);
        }
        throw new IllegalArgumentException("Unknown model class given: " + modelClass.getName());
    }
}
