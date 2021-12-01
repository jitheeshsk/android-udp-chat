package com.hiskysat.udpchat;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.AccountDto;
import com.hiskysat.entity.Account;
import com.hiskysat.ports.api.AccountServicePort;

public class MainActivityViewModel extends ViewModel {

    public static final int BOOT_CREATE_ACCOUNT = 1;
    public static final int BOOT_MAIN_PAGE = 2;

    private final AccountServicePort accountService;
    private final MutableLiveData<Account> userAccount = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> bootNavigationEvent = new MutableLiveData<>();

    MainActivityViewModel(Application application, AccountServicePort accountServicePort) {
        this.accountService = accountServicePort;
    }

    public void start() {
        AccountDto accountDto = this.accountService.getCurrentUser();
        this.bootNavigationEvent.setValue(new Event<>(accountDto == null ? BOOT_CREATE_ACCOUNT : BOOT_MAIN_PAGE));
    }

    public MutableLiveData<Event<Integer>> getBootNavigationEvent() {
        return bootNavigationEvent;
    }
}
