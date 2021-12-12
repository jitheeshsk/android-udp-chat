package com.hiskysat.udpchat.util.rx;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class RxDisposableHelper {

    private final CompositeDisposable disposables = new CompositeDisposable();


    public void add(Disposable disposable) {
        disposables.add(disposable);
    }

    public void clear() {
        disposables.clear();
        disposables.dispose();
    }

}
