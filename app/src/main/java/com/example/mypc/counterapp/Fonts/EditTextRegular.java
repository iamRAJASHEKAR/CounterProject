package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class EditTextRegular extends AppCompatEditText
{
    public EditTextRegular(Context context) {
        super(context);
        init();
    }

    public EditTextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextRegular(Context context, AttributeSet attrs, int defStyleAttr) {
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
