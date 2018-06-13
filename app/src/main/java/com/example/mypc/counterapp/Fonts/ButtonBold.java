package com.example.mypc.counterapp.Fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

public class ButtonBold extends AppCompatButton
{
    public ButtonBold(Context context) {
        super(context);
        init();
    }

    public ButtonBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonBold(Context context, AttributeSet attrs, int defStyleAttr) {
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
