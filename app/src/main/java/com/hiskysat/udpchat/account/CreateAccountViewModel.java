package com.hiskysat.udpchat.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hiskysat.udpchat.data.Account;

public class CreateAccountViewModel extends ViewModel {

    private MutableLiveData<Account> userAccount;

    public LiveData<Account> getUserAccount() {
        return userAccount;
    }



}
