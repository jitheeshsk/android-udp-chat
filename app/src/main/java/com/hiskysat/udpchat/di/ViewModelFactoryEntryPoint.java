package com.hiskysat.udpchat.di;

import com.hiskysat.udpchat.ViewModelFactory;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@EntryPoint
@InstallIn(SingletonComponent.class)
public interface ViewModelFactoryEntryPoint {
    ViewModelFactory viewModelFactory();
}
