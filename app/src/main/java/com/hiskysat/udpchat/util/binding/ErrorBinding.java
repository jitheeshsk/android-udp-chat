package com.hiskysat.udpchat.util.binding;

import androidx.databinding.BindingAdapter;

import com.google.android.material.textfield.TextInputLayout;

public class ErrorBinding {

    @BindingAdapter("app:error")
    public static void setError(TextInputLayout layout, String error) {
        layout.setError(error);
    }

}
