package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewHeavy extends AppCompatTextView {

    public TextViewHeavy(Context context) {
        super(context);
        init();
    }

    public TextViewHeavy(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewHeavy(Context context, AttributeSet attrs, int defStyleAttr) {
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
