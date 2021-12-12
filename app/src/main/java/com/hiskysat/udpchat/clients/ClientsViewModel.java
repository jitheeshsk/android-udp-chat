package com.hiskysat.udpchat.clients;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.hiskysat.data.ClientDto;
import com.hiskysat.ports.api.ClientServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.R;
import com.hiskysat.udpchat.data.Client;
import com.hiskysat.udpchat.data.Placeholder;
import com.hiskysat.udpchat.mapper.ClientMapper;
import com.hiskysat.udpchat.util.rx.RxViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class ClientsViewModel extends RxViewModel {

    private static final long NEW_ITEM_ID = -1;

    private final ClientServicePort clientServicePort;
    private final BehaviorSubject<Boolean> refreshEvent = BehaviorSubject.create();

    private final MutableLiveData<Placeholder> placeholder = new MutableLiveData<>(Placeholder.builder()
            .iconResId(R.drawable.ic_add)
            .labelResId(R.string.no_clients)
            .buttonLabelResId(R.string.no_clients_add)
            .showButton(true)
            .buttonClickListener(this::openAddNewClient)
            .build());

    private final MutableLiveData<List<Client>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> dataLoading = new MutableLiveData<>();
    public final LiveData<Boolean> empty = Transformations.map(items, (List::isEmpty));
    public final ObservableBoolean isDataLoadingError = new ObservableBoolean();

    private final MutableLiveData<Event<Long>> openAddClientEvent = new MutableLiveData<>();
    private final MutableLiveData<Event<Long>> openMessageClientEvent = new MutableLiveData<>();

    public ClientsViewModel(ClientServicePort clientServicePort) {
        this.clientServicePort = clientServicePort;
        initObservables();
    }

    public MutableLiveData<List<Client>> getItems() {
        return items;
    }

    public LiveData<Placeholder> getPlaceholder() {
        return placeholder;
    }

    public LiveData<Event<Long>> getOpenAddClientEvent() {
        return openAddClientEvent;
    }

    public LiveData<Event<Long>> getOpenMessageClientEvent() {
        return openMessageClientEvent;
    }

    public LiveData<Boolean> getEmpty() {
        return empty;
    }

    private void initObservables() {
        Disposable refreshEvent = this.refreshEvent
                .toFlowable(BackpressureStrategy.BUFFER)
                .switchMap(refresh -> loadClients(true))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items::setValue);
        addDisposable(refreshEvent);
    }

    public void start() {
        this.refreshEvent.onNext(true);
    }

    public Flowable<List<Client>> loadClients(boolean showLoadingUi) {
        return this.clientServicePort.getClients()
                .map(this::toClients)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(subscription -> {
                    isDataLoadingError.set(false);
                    if (showLoadingUi) {
                        dataLoading.setValue(true);
                    }
                })
                .doAfterNext(subscription -> dataLoading.setValue(false));
    }

    public void openAddNewClient() {
        openAddClientEvent.setValue(new Event<>(NEW_ITEM_ID));
    }

    public void openMessageClient(Long clientId) {
        openMessageClientEvent.setValue(new Event<>(clientId));
    }

    private List<Client> toClients(List<ClientDto> clientDtos) {
        List<Client> chats = new ArrayList<>();
        for (ClientDto client: clientDtos) {
            chats.add(ClientMapper.clientFromClientDto(client));
        }
        return chats;
    }


}
