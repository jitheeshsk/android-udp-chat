package com.hiskysat.udpchat;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.AccountDto;
import com.hiskysat.entity.Account;
import com.hiskysat.ports.api.AccountServicePort;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends ViewModel {

    public static final int BOOT_CREATE_ACCOUNT = 1;
    public static final int BOOT_MAIN_PAGE = 2;

    public static final int ID_NO_ACCOUNT_FOUND = -1;

    private final AccountServicePort accountService;
    private final MutableLiveData<Event<Integer>> bootNavigationEvent = new MutableLiveData<>();

    private final MutableLiveData<Event<Long>> accountFoundEvent = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    MainActivityViewModel(AccountServicePort accountServicePort) {
        this.accountService = accountServicePort;
    }

    public void start() {
       Disposable accountDisposable = this.accountService.getCurrentUser()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .toSingle()
            .subscribe((accountDto, throwable) -> this.accountFoundEvent.setValue(new Event<>(accountDto == null ? ID_NO_ACCOUNT_FOUND : accountDto.getId())));
       disposables.add(accountDisposable);

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        disposables.dispose();
    }

    public MutableLiveData<Event<Integer>> getBootNavigationEvent() {
        return bootNavigationEvent;
    }

    public MutableLiveData<Event<Long>> getAccountFoundEvent() {
        return accountFoundEvent;
    }
}
