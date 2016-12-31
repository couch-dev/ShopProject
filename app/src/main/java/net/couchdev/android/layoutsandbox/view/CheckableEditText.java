package net.couchdev.android.layoutsandbox.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;

import net.couchdev.android.layoutsandbox.R;

import java.util.ArrayList;

/**
 * Created by Tim on 28.12.2016.
 */

public class CheckableEditText extends EditText implements Checkable, View.OnFocusChangeListener, View.OnTouchListener, TextView.OnEditorActionListener {

    private boolean checked = false;
    private int textColor;
    private Drawable background;
    private String text;
    private int inputType;
    private InputFilter[] filters;
    private boolean hasError;
    private int[] padding = new int[4];
    private ArrayList<InputErrorHandler> inputErrorHandlers;
    private TextWatcher textWatcher;

    public CheckableEditText(Context context) {
        super(context);
        init();
    }

    public CheckableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CheckableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        textColor = getCurrentTextColor();
        background = getBackground();
        text = getText().toString();
        inputType = getInputType();
        filters = getFilters();
        padding = new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
        setOnEditorActionListener(this);
        inputErrorHandlers = new ArrayList<>();
    }

    private static final int[] CheckedStateSet = {
            android.R.attr.state_checked
    };

    public void setInputFilter(TextWatcher textWatcher){
        this.textWatcher = textWatcher;
        addTextChangedListener(textWatcher);
    }

    public void addInputErrorHandler(InputErrorHandler inputErrorHandler){
        inputErrorHandlers.add(inputErrorHandler);
        setOnFocusChangeListener(this);
        setOnTouchListener(this);
    }

    public void removeInputErrorHandlers(){
        inputErrorHandlers = new ArrayList<>();
        setOnFocusChangeListener(null);
        setOnTouchListener(null);
    }

    private void setError(String errorText){
        textColor = getCurrentTextColor();
        background = getBackground();
        text = getText().toString();
        inputType = getInputType();
        filters = getFilters();
        padding = new int[]{getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom()};
        removeTextChangedListener(textWatcher);
        setInputType(InputType.TYPE_CLASS_TEXT);
        setFilters(new InputFilter[]{});
        setBackgroundResource(R.drawable.edittext_selector_error);
        setTextColor(getContext().getResources().getColor(R.color.white));
        setText(errorText);
        setPadding(padding[0], padding[1], padding[2], padding[3]);
        setChecked(false);
        hasError = true;
    }

    private void resolveError(){
        if(textWatcher != null){
            addTextChangedListener(textWatcher);
        }
        setInputType(inputType);
        setFilters(filters);
        setBackground(background);
        setTextColor(textColor);
        setText(text);
        setPadding(padding[0], padding[1], padding[2], padding[3]);
        hasError = false;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CheckedStateSet);
        }
        return drawableState;
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(getText().toString().isEmpty()){
            setChecked(false);
        }
        if(!hasFocus && !getText().toString().isEmpty()){
            for(InputErrorHandler ieh: inputErrorHandlers) {
                if (ieh.errorCondition()) {
                    if (!hasError) {
                        setError(ieh.errorText());
                        ieh.onInputError();
                        setChecked(false);
                        return;
                    }
                } else {
                    setChecked(true);
                }
            }
        }
        if(hasFocus && hasError){
            resolveError();
            setSelection(getText().toString().length());
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(hasError && event.getAction() == MotionEvent.ACTION_DOWN){
            resolveError();
            requestFocus();
            setSelection(getText().toString().length());
            return true;
        }
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId==EditorInfo.IME_ACTION_DONE){
            giveUpFocus();
        }
        return false;
    }

    private void giveUpFocus(){
        clearFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    public interface InputErrorHandler {
        String errorText();
        boolean errorCondition();
        void onInputError();
    }
}
