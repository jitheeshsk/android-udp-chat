package com.hiskysat.udpchat.account;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hiskysat.data.AccountDto;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Account;
import com.hiskysat.udpchat.util.AppValidation;
import com.hiskysat.udpchat.util.rx.RxViewModel;
import com.hiskysat.udpchat.util.validator.LiveDataValidator;
import com.hiskysat.udpchat.util.validator.LiveDataValidatorResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.BiConsumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CreateAccountViewModel extends RxViewModel {

    private final MutableLiveData<Boolean> readOnly = new MutableLiveData<>(false);

    private final MutableLiveData<String> ipAddress = new MutableLiveData<>();
    private final MutableLiveData<Integer> ipAddressError = new MutableLiveData<>();

    private final MutableLiveData<String> portNumber = new MutableLiveData<>();
    private final LiveDataValidator portNumberValidator = new LiveDataValidator(portNumber) {{
        addRule(R.string.msg_port_number_required, TextUtils::isEmpty);
        addRule(R.string.msg_port_number_invalid, AppValidation::isNotValidNumber);
        addRule(R.string.msg_port_number_not_within_range, s -> !AppValidation.isWithPortRange(s));
    }};

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

    private final MutableLiveData<Event<Long>> userCreatedEvent = new MutableLiveData<>();

    private final AccountServicePort accountServicePort;

    public CreateAccountViewModel(AccountServicePort accountServicePort) {
        this.accountServicePort = accountServicePort;
    }

    public MutableLiveData<String> getIpAddress() {
        return ipAddress;
    }

    public MutableLiveData<String> getPortNumber() {
        return portNumber;
    }

    public LiveDataValidator getPortNumberValidator() {
        return portNumberValidator;
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

    public LiveData<Boolean> getIsReadOnly() {
        return readOnly;
    }

    public LiveData<Integer> getIpAddressError() {
        return ipAddressError;
    }

    public LiveData<Event<Long>> getUserCreatedEvent() {
        return userCreatedEvent;
    }


    public void start(boolean isReadOnly) {
        readOnly.setValue(isReadOnly);
        Disposable ipAddressSub = this.accountServicePort.getCurrentIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ipAddress, throwable) -> {
                   if (throwable != null) {
                       this.ipAddress.setValue(null);
                       this.ipAddressError.setValue(R.string.ip_address_get_failed);
                       return;
                   }
                   this.ipAddressError.setValue(null);
                   this.ipAddress.setValue(ipAddress);
                });
        addDisposable(ipAddressSub);

        if (!isReadOnly){
            this.isCreateAccountFormValidMediator.setValue(false);
            this.isCreateAccountFormValidMediator.addSource(userName, s -> validateForm());
            this.isCreateAccountFormValidMediator.addSource(portNumber, s -> validateForm());
            this.isCreateAccountFormValidMediator.addSource(password, s -> validateForm());
            this.isCreateAccountFormValidMediator.addSource(confirmPassword, s -> validateForm());
        }
        else {
           Disposable userSub = this.accountServicePort.getCurrentUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(accountDto -> {
                        this.userName.setValue(accountDto.getUserName());
                        this.portNumber.setValue(String.valueOf(accountDto.getPort()));
                    });
           addDisposable(userSub);
        }

    }

    public void stop() {
        if (this.readOnly.getValue() == null || !this.readOnly.getValue()) {
            this.isCreateAccountFormValidMediator.removeSource(userName);
            this.isCreateAccountFormValidMediator.removeSource(portNumber);
            this.isCreateAccountFormValidMediator.removeSource(password);
            this.isCreateAccountFormValidMediator.removeSource(confirmPassword);
        }

    }


    public void createUserAccount() {
        Boolean isValid = isCreateAccountFormValidMediator.getValue();
        if (isValid != null && !isValid) {
            return;
        }
        AccountDto dto = AccountDto.builder()
                .userName(userName.getValue())
                .password(password.getValue())
                .port(Integer.parseInt(Objects.requireNonNull(portNumber.getValue())))
                .build();
        Disposable accountDisposable = this.accountServicePort.createAccount(dto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(accountDto -> this.userCreatedEvent.setValue(new Event<>(accountDto.getId())));
        addDisposable(accountDisposable);

    }


    private void validateForm() {
        List<LiveDataValidator> validators = Arrays.asList(portNumberValidator,
                userNameValidator, passwordValidator, confirmPasswordValidator);
        LiveDataValidatorResolver resolver = new LiveDataValidatorResolver(validators);
        this.isCreateAccountFormValidMediator.setValue(resolver.isValid());
    }

}
