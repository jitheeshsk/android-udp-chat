package com.hiskysat.udpchat.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Placeholder {
    @DrawableRes
    private int iconResId;
    @StringRes
    private int iconContentDescription;
    @StringRes
    private int labelResId;
    @StringRes
    private int buttonLabelResId;
    private boolean showButton;
    private OnButtonClickListener buttonClickListener;


    public interface OnButtonClickListener {
        void onButtonClicked();
    }


}
