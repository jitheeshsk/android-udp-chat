package com.hiskysat.udpchat.data;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import lombok.Builder;
import lombok.Data;

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

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public int getIconContentDescription() {
        return iconContentDescription;
    }

    public void setIconContentDescription(int iconContentDescription) {
        this.iconContentDescription = iconContentDescription;
    }

    public int getLabelResId() {
        return labelResId;
    }

    public void setLabelResId(int labelResId) {
        this.labelResId = labelResId;
    }

    public int getButtonLabelResId() {
        return buttonLabelResId;
    }

    public void setButtonLabelResId(int buttonLabelResId) {
        this.buttonLabelResId = buttonLabelResId;
    }

    public boolean isShowButton() {
        return showButton;
    }

    public void setShowButton(boolean showButton) {
        this.showButton = showButton;
    }

    public OnButtonClickListener getButtonClickListener() {
        return buttonClickListener;
    }

    public void setButtonClickListener(OnButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public interface OnButtonClickListener {
        void onButtonClicked();
    }


}
