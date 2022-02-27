package com.jumbosouq.com.customdesigns;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {

    private String BOLD = "Poppins-Bold.ttf";
    private String LIGHT = "Poppins-Light.ttf";
    private String REGULAR = "Poppins-Regular.ttf";
    private String MEDIUM = "Poppins-Medium.ttf";
    private String BOLDG = "GothamBold.ttf";

    Typeface bold;
    Typeface light;
    Typeface regular;
    Typeface medium;
    Typeface GBold;

    public TypeFactory(Context context) {
        bold = Typeface.createFromAsset(context.getAssets(), BOLD);
        light = Typeface.createFromAsset(context.getAssets(), LIGHT);
        regular = Typeface.createFromAsset(context.getAssets(), REGULAR);
        medium = Typeface.createFromAsset(context.getAssets(), MEDIUM);
        GBold = Typeface.createFromAsset(context.getAssets(), BOLDG);
    }
}