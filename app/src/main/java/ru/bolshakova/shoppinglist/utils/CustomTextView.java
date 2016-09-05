package ru.bolshakova.shoppinglist.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {


    public CustomTextView(Context context) {
        super(context);
        setTypeface(CustomFont.getInstance(context).getTypeFace());
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(CustomFont.getInstance(context).getTypeFace());
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(CustomFont.getInstance(context).getTypeFace());
    }
}
