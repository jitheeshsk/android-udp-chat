package com.hiskysat.udpchat.addclient;

import android.text.TextUtils;
import android.util.Pair;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.hiskysat.data.ClientDto;
import com.hiskysat.ports.api.ClientServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.util.AppValidation;
import com.hiskysat.udpchat.util.rx.RxViewModel;
import com.hiskysat.udpchat.util.validator.LiveDataValidator;
import com.hiskysat.udpchat.util.validator.LiveDataValidatorResolver;

import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lombok.AllArgsConstructor;
import lombok.Data;

public class AddClientViewModel extends RxViewModel {

    private final MutableLiveData<String> ipAddress = new MutableLiveData<>();
    private final LiveDataValidator ipAddressValidator = new LiveDataValidator(ipAddress){{
        addRule(R.string.msg_ip_address_required, TextUtils::isEmpty);
        addRule(R.string.msg_ip_address_invalid, i -> !Patterns.IP_ADDRESS.matcher(i).matches());
    }};

    private final MutableLiveData<String> portNumber = new MutableLiveData<>();
    private final LiveDataValidator portNumberValidator = new LiveDataValidator(portNumber){{
        addRule(R.string.msg_port_number_required, TextUtils::isEmpty);
        addRule(R.string.msg_port_number_invalid, AppValidation::isNotValidNumber);
        addRule(R.string.msg_port_number_not_within_range, p -> !AppValidation.isWithPortRange(p));
    }};

    private final MutableLiveData<Integer> addClientError = new MutableLiveData<>();

    private final MediatorLiveData<Boolean> isAddClientFormValidMediator = new MediatorLiveData<>();

    private final ClientServicePort clientServicePort;

    private final MutableLiveData<Event<ClientAddEvent>> clientAddedEvent = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> clientAddFailedEvent = new MutableLiveData<>();

    private final PublishSubject<Boolean> validateForm = PublishSubject.create();

    public AddClientViewModel(ClientServicePort clientServicePort) {
        this.clientServicePort = clientServicePort;
    }

    public MutableLiveData<String> getIpAddress() {
        return ipAddress;
    }

    public LiveDataValidator getIpAddressValidator() {
        return ipAddressValidator;
    }

    public MutableLiveData<String> getPortNumber() {
        return portNumber;
    }

    public LiveDataValidator getPortNumberValidator() {
        return portNumberValidator;
    }

    public LiveData<Event<ClientAddEvent>> getClientAddedEvent() {
        return clientAddedEvent;
    }

    public LiveData<Event<Boolean>> getClientAddFailedEvent() {
        return clientAddFailedEvent;
    }

    public LiveData<Integer> getAddClientError() {
        return addClientError;
    }

    public LiveData<Boolean> getIsAddClientFormValidMediator() {
        return isAddClientFormValidMediator;
    }



    public void initSetup() {
        Disposable validationSub = validateForm
                .toFlowable(BackpressureStrategy.BUFFER)
                .flatMap(event -> startFormValidation())
                .observeOn(Schedulers.io())
                .flatMapSingle(validForm -> validateClientNotExists()
                        .map(clientNoExists -> new Pair<>(validForm, clientNoExists)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(validation -> {
                    if (!validation.second) {
                        addClientError.setValue(R.string.msg_add_client_ip_exists);
                    } else {
                        addClientError.setValue(null);
                    }
                    isAddClientFormValidMediator.setValue(validation.first && validation.second);
                });
        addDisposable(validationSub);
    }

    public void start() {
        this.isAddClientFormValidMediator.addSource(ipAddress, s -> validateForm());
        this.isAddClientFormValidMediator.addSource(portNumber, s -> validateForm());
    }

    public void stop() {
        this.isAddClientFormValidMediator.removeSource(ipAddress);
        this.isAddClientFormValidMediator.removeSource(portNumber);
    }

    public void addClient() {

        String portNumberVal = portNumber.getValue();
        if (portNumberVal == null || TextUtils.isEmpty(portNumberVal)) {
            return;
        }

        Disposable clientAddSub = this.clientServicePort
                .addClient(ipAddress.getValue(), Integer.parseInt(portNumberVal))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((clientDto, throwable) -> {
                    if (throwable != null) {
                        clientAddFailedEvent.setValue(new Event<>(true));
                        return;
                    }
                    clientAddedEvent.setValue(
                            new Event<>(ClientAddEvent.of(clientDto.getIpAddress(), clientDto.getId()))
                    );
                });
        addDisposable(clientAddSub);
    }

    private Flowable<Boolean> startFormValidation() {
        return Flowable.create(emitter -> {
            List<LiveDataValidator> validators = Arrays.asList(ipAddressValidator, portNumberValidator);
            LiveDataValidatorResolver resolver = new LiveDataValidatorResolver(validators);
            emitter.onNext(resolver.isValid());
        }, BackpressureStrategy.BUFFER);
    }

    private Single<Boolean> validateClientNotExists() {
        return clientValidateWithIp()
                .flatMap(value -> {
                    if (value.second) {
                        return clientDoesNotExist(value.first);
                    }
                    return Single.just(false);
                });
    }

    private Single<Pair<String, Boolean>> clientValidateWithIp() {
        return Single.create(emitter -> {
            Pair<String, Boolean> pair =
                    Pair.create(ipAddress.getValue(), ipAddressValidator.isValidWithoutEmitting());
            emitter.onSuccess(pair);
        });
    }

    private Single<Boolean> clientDoesNotExist(String ipAddress) {
       return clientServicePort.getClientByIpAddress(ipAddress)
               .switchIfEmpty(Single.just(ClientDto.builder().build()))
               .map(clientDto -> clientDto.getId() == null);
    }


    private void validateForm() {
        validateForm.onNext(true);
    }

    @AllArgsConstructor(staticName = "of")
    @Data
    protected static class ClientAddEvent {
        String name;
        Long id;
    }

}
