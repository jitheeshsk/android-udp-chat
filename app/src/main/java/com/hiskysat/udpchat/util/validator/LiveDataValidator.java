package com.hiskysat.udpchat.util.validator;

import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class LiveDataValidator {

    private final LiveData<String> liveData;
    private final MutableLiveData<Integer> error = new MutableLiveData<>();

    private final List<Predicate<String>> validationRules = new ArrayList<>();
    private final List<Integer> errorMessages = new ArrayList<>();

    public LiveDataValidator(LiveData<String> liveData) {
        this.liveData = liveData;
    }

    public MutableLiveData<Integer> getError() {
        return error;
    }

    public boolean isValid() {
        for (int i = 0; i < validationRules.size(); i++) {
            if (validationRules.get(i).test(liveData.getValue())) {
                emitErrorMessage(errorMessages.get(i));
                return false;
            }
        }
        emitErrorMessage(null);
        return true;
    }

    //For emitting error messages
    private void emitErrorMessage(@StringRes Integer messageRes) {
        error.setValue(messageRes);
    }

    public void addRule(@StringRes Integer errorMessage, Predicate<String> predicate) {
        this.validationRules.add(predicate);
        this.errorMessages.add(errorMessage);
    }

}
