package net.couchdev.android.layoutsandbox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.EditText;

/**
 * Created by Tim on 28.12.2016.
 */

public class CheckableEditText extends EditText implements Checkable{

    private boolean checked = false;

    public CheckableEditText(Context context) {
        super(context);
    }

    public CheckableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked
    };

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
    }
}
