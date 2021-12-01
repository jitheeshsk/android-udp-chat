package com.hiskysat.udpchat.account;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.AccountDto;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Account;
import com.hiskysat.udpchat.util.validator.LiveDataValidator;
import com.hiskysat.udpchat.util.validator.LiveDataValidatorResolver;

import java.util.Arrays;
import java.util.List;

public class CreateAccountViewModel extends ViewModel {

    private final MutableLiveData<String> ipAddress = new MutableLiveData<>();
    private final MutableLiveData<String> userName = new MutableLiveData<>();
    private final LiveDataValidator userNameValidator = new LiveDataValidator(userName){{
        addRule(R.string.msg_user_name_required, TextUtils::isEmpty);
        addRule(R.string.msg_user_name_length, s -> s.length() < 5);
    }};

    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final LiveDataValidator passwordValidator = new LiveDataValidator(password){{
        addRule(R.string.msg_password_required, TextUtils::isEmpty);
        addRule(R.string.msg_password_length, s -> s.length() < 8);
    }};

    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final LiveDataValidator confirmPasswordValidator = new LiveDataValidator(confirmPassword){{
        addRule(R.string.msg_password_required, TextUtils::isEmpty);
        addRule(R.string.msg_password_length, s -> s.length() < 8);
        addRule(R.string.msg_password_mismatch, s -> !s.equals(password.getValue()));
    }};

    private final MediatorLiveData<Boolean> isCreateAccountFormValidMediator = new MediatorLiveData<>();

    private final AccountServicePort accountServicePort;

    public CreateAccountViewModel(AccountServicePort accountServicePort) {
        this.accountServicePort = accountServicePort;
    }

    public MutableLiveData<String> getIpAddress() {
        return ipAddress;
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public LiveDataValidator getUserNameValidator() {
        return userNameValidator;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public LiveDataValidator getPasswordValidator() {
        return passwordValidator;
    }

    public MutableLiveData<String> getConfirmPassword() {
        return confirmPassword;
    }

    public LiveDataValidator getConfirmPasswordValidator() {
        return confirmPasswordValidator;
    }

    public MediatorLiveData<Boolean> getIsCreateAccountFormValidMediator() {
        return isCreateAccountFormValidMediator;
    }

    public void start() {
        this.isCreateAccountFormValidMediator.setValue(false);
        this.isCreateAccountFormValidMediator.addSource(userName, s -> validateForm());
        this.isCreateAccountFormValidMediator.addSource(password, s -> validateForm());
        this.isCreateAccountFormValidMediator.addSource(confirmPassword, s -> validateForm());
    }

    public void stop() {
        this.isCreateAccountFormValidMediator.removeSource(userName);
        this.isCreateAccountFormValidMediator.removeSource(password);
        this.isCreateAccountFormValidMediator.removeSource(confirmPassword);
    }

    private void validateForm() {
        List<LiveDataValidator> validators = Arrays.asList(userNameValidator, passwordValidator, confirmPasswordValidator);
        LiveDataValidatorResolver resolver = new LiveDataValidatorResolver(validators);
        this.isCreateAccountFormValidMediator.setValue(resolver.isValid());
    }

    public void createUserAccount() {
        Boolean isValid = isCreateAccountFormValidMediator.getValue();
        if (isValid != null && !isValid) {
            return;
        }
        AccountDto dto = new AccountDto();
        dto.setUserName(userName.getValue());
        dto.setPassword(password.getValue());
        this.accountServicePort.createAccount(dto);

    }



}
