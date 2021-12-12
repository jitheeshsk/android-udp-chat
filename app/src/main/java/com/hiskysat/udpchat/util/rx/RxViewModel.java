package com.hiskysat.udpchat.util.rx;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.Disposable;

public class RxViewModel extends ViewModel {

    private final RxDisposableHelper rxDisposableHelper = new RxDisposableHelper();

    protected void addDisposable(Disposable disposable) {
        rxDisposableHelper.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        rxDisposableHelper.clear();
    }

}
