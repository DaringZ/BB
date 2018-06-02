package com.devster.bloodybank.Registeration.TextValidation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

/**
 * Created by MOD on 5/3/2018.
 */

public abstract class Validation implements TextWatcher {

    private Context context;
    private String _before;
    private String _old;
    private String _new;
    private String _after;


    private boolean _ignore = false;

    @Override
    public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
        _before = sequence.subSequence(0,start).toString();
        _old = sequence.subSequence(start, start+count).toString();
        _after = sequence.subSequence(start+count, sequence.length()).toString();
    }

    @Override
    public void onTextChanged(CharSequence sequence, int start, int before, int count) {
        _new = sequence.subSequence(start, start+count).toString();
    }

    @Override
    public void afterTextChanged(Editable sequence) {
        if (_ignore)
            return;

        onTextChanged(_before, _old, _new, _after);
    }


    protected abstract void onTextChanged(String before, String old, String aNew, String after);


    protected void startUpdates(){
        _ignore = true;
    }

    protected void endUpdates(){
        _ignore = false;
    }
}
