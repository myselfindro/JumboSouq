package com.jumbosouq.com.customdesigns;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.jumbosouq.com.R;


public class CustomTextView extends AppCompatTextView {

    private int typefaceType;
    private com.jumbosouq.com.customdesigns.TypeFactory mFontFactory;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    public CustomTextView(Context context) {
        super(context);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {

        TypedArray array = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView,
                0, 0);
        try {
            typefaceType = array.getInteger(R.styleable.CustomTextView_font_name, 0);
        } finally {
            array.recycle();
        }
        if (!isInEditMode()) {
            setTypeface(getTypeFace(typefaceType));
        }
    }

    public Typeface getTypeFace(int type) {
        if (mFontFactory == null)
            mFontFactory = new com.jumbosouq.com.customdesigns.TypeFactory(getContext());

        switch (type) {
            case Constants.BOLD:
                return mFontFactory.bold;

            case Constants.MEDIUM:
                return mFontFactory.medium;

            case Constants.REGULAR:
                return mFontFactory.regular;

            case Constants.LIGHT:
                return mFontFactory.light;

            default:
                return mFontFactory.regular;
        }
    }

    public interface Constants {
        int BOLD = 1,
                MEDIUM = 2,
                REGULAR = 3,
                LIGHT = 4;

    }
}
