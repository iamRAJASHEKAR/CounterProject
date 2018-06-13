package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditTextBold extends AppCompatEditText
{

    public EditTextBold(Context context) {
        super(context);
        init();
    }

    public EditTextBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextBold(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/Lato-Bold.ttf");
        setTypeface(tf);
    }
}
