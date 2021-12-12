package com.hiskysat.udpchat.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hiskysat.data.MessageWrapper;
import com.hiskysat.ports.api.AccountServicePort;
import com.hiskysat.ports.api.ChatServicePort;
import com.hiskysat.udpchat.Event;
import com.hiskysat.udpchat.data.Account;
import com.hiskysat.udpchat.data.NotificationMessage;
import com.hiskysat.udpchat.util.rx.RxDisposableHelper;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatViewModel {

    private final RxDisposableHelper disposableHelper = new RxDisposableHelper();

    private final MutableLiveData<Account> account = new MutableLiveData<>();
    private final MutableLiveData<Event<NotificationMessage>> messageReceived = new MutableLiveData<>();


    private final ChatServicePort chatService;
    private final AccountServicePort accountService;

    @Inject
    public ChatViewModel(ChatServicePort chatService, AccountServicePort accountService) {
        this.chatService = chatService;
        this.accountService = accountService;
    }

    public LiveData<Account> getAccount() {
        return account;
    }

    public LiveData<Event<NotificationMessage>> getMessageReceived() {
        return messageReceived;
    }

    public void start() {
        Disposable messageSub = this.accountService.getLiveUser()
                .zipWith(accountService.getCurrentIp().toFlowable(), (accountDto, ipAddress) -> Account.builder()
                        .ipAddress(ipAddress)
                        .name(accountDto.getUserName())
                        .port(accountDto.getPort())
                        .build())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this.account::setValue)
                .observeOn(Schedulers.io())
                .switchMap(account -> chatService.startListening())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageWrapper -> messageReceived.setValue(new Event<>(fromMessageWrapper(messageWrapper))));




        disposableHelper.add(messageSub);
    }

    private NotificationMessage fromMessageWrapper(MessageWrapper wrapper) {
        return NotificationMessage.builder()
                .chatId(wrapper.getChatId())
                .ipAddress(wrapper.getChatAddress().getIpAddress())
                .content(wrapper.getMessageDto().getMessage())
                .build();
    }

    public void destroy() {
        disposableHelper.clear();
    }
}
