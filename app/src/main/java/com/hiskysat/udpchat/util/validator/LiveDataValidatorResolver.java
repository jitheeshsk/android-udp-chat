package com.hiskysat.udpchat.util.validator;

import java.util.List;

public class LiveDataValidatorResolver {

    private final List<LiveDataValidator> validators;

    public LiveDataValidatorResolver(List<LiveDataValidator> validators) {
        this.validators = validators;
    }

    public boolean isValid() {
        for (LiveDataValidator validator: validators) {
            if (!validator.isValid()) return false;
        }
        return true;
    }

}
