package com.example.android.donebar;

/**
 * Created by Kaikisaragi on 5/27/2015.
 */
import android.widget.EditText;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyEditText extends EditText {
    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/gothic_0.ttf");
            setTypeface(tf);
        }
    }
}