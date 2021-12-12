package com.hiskysat.udpchat.util.fragment;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FragmentHelper {

    public static void setTitle(Fragment fragment, @StringRes int titleResId) {
        setTitle(fragment, fragment.getString(titleResId));
    }

    public static void setTitle(Fragment fragment, String title) {
        AppCompatActivity activity = (AppCompatActivity) fragment.requireActivity();
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle(title);
        }
    }

}
