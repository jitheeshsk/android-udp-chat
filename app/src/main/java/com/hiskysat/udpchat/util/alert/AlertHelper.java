package com.hiskysat.udpchat.util.alert;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;

public class AlertHelper {

    public static void showToast(@NonNull View view, String message, int duration) {
        Snackbar.make(view, message, duration)
                .show();
    }

    public static void showToast(@NonNull View view, String message) {
        showToast(view, message, Snackbar.LENGTH_LONG);
    }

    public static void showToast(@NonNull View view, @StringRes int messageId) {
        Snackbar.make(view, messageId, Snackbar.LENGTH_LONG)
                .show();
    }

}
