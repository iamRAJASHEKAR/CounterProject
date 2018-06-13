package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class ButtonHeavy extends AppCompatButton {
    public ButtonHeavy(Context context) {
        super(context);
        init();
    }

    public ButtonHeavy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonHeavy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Heavy.ttf");
        setTypeface(tf);
    }
}
