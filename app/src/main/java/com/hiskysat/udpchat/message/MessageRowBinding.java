package com.hiskysat.udpchat.message;

import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.BindingAdapter;

public class MessageRowBinding {

    @BindingAdapter("app:horizontalBias")
    public static void setHorizontalBias(View view, float bias) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ConstraintLayout.LayoutParams) {
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
            params.horizontalBias = bias; // here is one modification for example. modify anything else you want :)
            view.setLayoutParams(params);
        }

    }
}
