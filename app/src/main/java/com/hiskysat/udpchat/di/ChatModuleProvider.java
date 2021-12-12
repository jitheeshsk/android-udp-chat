package com.hiskysat.udpchat.di;

import com.hiskysat.adapters.ChatInterface;
import com.hiskysat.data.AccountDto;
import com.hiskysat.ports.spi.AccountPersistencePort;
import com.hiskysat.ports.spi.ChatInterfacePort;

import java.net.SocketException;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

@InstallIn(ServiceComponent.class)
@Module
public class ChatModuleProvider {



}
