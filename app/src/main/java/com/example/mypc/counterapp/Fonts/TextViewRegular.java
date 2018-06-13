package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRegular extends android.support.v7.widget.AppCompatTextView
{


    public TextViewRegular(Context context) {
        super(context);
        init();
    }

    public TextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Regular.ttf");
        setTypeface(tf);
    }
}
