package com.hiskysat.udpchat.di;

import android.content.Context;

import com.hiskysat.adapters.ChatInterface;
import com.hiskysat.adapters.room.AccountRoomAdapter;
import com.hiskysat.adapters.room.ChatRoomAdapter;
import com.hiskysat.adapters.room.ClientRoomAdapter;
import com.hiskysat.adapters.room.MessageRoomAdapter;
import com.hiskysat.adapters.util.HashServiceAdapter;
import com.hiskysat.adapters.util.NetworkInterfaceAdapter;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.ports.api.ClientServicePort;
import com.hiskysat.ports.api.HashServicePort;
import com.hiskysat.ports.api.MessageServicePort;
import com.hiskysat.ports.spi.AccountPersistencePort;
import com.hiskysat.ports.spi.ChatInterfacePort;
import com.hiskysat.ports.spi.ChatPersistencePort;
import com.hiskysat.ports.spi.ClientPersistencePort;
import com.hiskysat.ports.spi.MessagePersistencePort;
import com.hiskysat.ports.spi.NetworkInterfacePort;
import com.hiskysat.service.AccountService;
import com.hiskysat.service.ChatService;
import com.hiskysat.service.ClientService;
import com.hiskysat.service.MessageService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModuleProvider {

    @Singleton
    @Provides
    public static Executor executor() {
        return Executors.newFixedThreadPool(2);
    }

    @Singleton
    @Provides
    public static AccountPersistencePort bindAccountRoom(@ApplicationContext Context context, Executor executor) {
        return new AccountRoomAdapter(context, executor);
    }

    @Singleton
    @Provides
    public static ClientPersistencePort bindClientRoom(@ApplicationContext Context context) {
        return new ClientRoomAdapter(context);
    }

    @Singleton
    @Provides
    public static ChatPersistencePort bindChatRoom(@ApplicationContext Context context) {
        return new ChatRoomAdapter(context);
    }

    @Singleton
    @Provides
    public static MessagePersistencePort bindMessageRoom(@ApplicationContext Context context) {
        return new MessageRoomAdapter(context);
    }

    @Singleton
    @Provides
    public static MessageServicePort bindMessageService(MessagePersistencePort messagePersistencePort) {
        return new MessageService(messagePersistencePort);
    }

    @Singleton
    @Provides
    public static HashServicePort getHashService() {
        return new HashServiceAdapter();
    }

    @Singleton
    @Provides
    public static NetworkInterfacePort getNetworkInterface() {
        return new NetworkInterfaceAdapter();
    }

    @Singleton
    @Provides
    public static AccountServicePort bindAccountService(AccountPersistencePort persistencePort, NetworkInterfacePort networkInterfacePort,
                                                        HashServicePort hashService) {
        return new AccountService(persistencePort, networkInterfacePort, hashService);
    }

    @Singleton
    @Provides
    public static ChatServicePort bindChatService(AccountServicePort accountService, ClientServicePort clientService,
                                                  MessageServicePort messageService, ChatPersistencePort chatPersistencePort,
                                                  ChatInterfacePort chatInterfacePort) {
        return new ChatService(accountService, clientService, messageService, chatPersistencePort, chatInterfacePort);
    }


    @Singleton
    @Provides
    public static ClientServicePort clientService(ClientPersistencePort clientPersistencePort) {
        return new ClientService(clientPersistencePort);
    }

    @Singleton
    @Provides
    public static ChatInterfacePort chatInterface() {
        return new ChatInterface();
    }

}
