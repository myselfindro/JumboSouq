package com.jumbosouq.com.Helper;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class Config {

    private static boolean isToastDevlopmentMode = true;
    public final static String LONG = "long";
    public final static String SHORT = "short";
    public final static String MIDDLE = "middle";
    public final static String TOP = "top";

    public static String token = "";
    public static String customertoken = "";
    public static String BaseURL = "https://www.jumbosouq.com/";  /*Live*/
    //public static String BaseURL = "http://www.jumbosouq.com/";  /*Live*/

    /*-----Online checking-----*/
    public static boolean isOnline(Context mContext) {
        ConnectivityManager conManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    /*-----Toast-----*/
    public static void showToast(Context context, String string, String length) {
        if (isToastDevlopmentMode) {
            switch (length) {
                case "long":
                    Toast.makeText(context, string, Toast.LENGTH_LONG).show();
                    break;
                case "short":
                    Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
                    break;
                case "middle":
                    Toast t_middle = Toast.makeText(context, string, Toast.LENGTH_SHORT);
                    t_middle.setGravity(Gravity.CENTER, 0, 0);
                    t_middle.show();
                    break;
                case "top":
                    Toast t_top = Toast.makeText(context, string, Toast.LENGTH_SHORT);
                    t_top.setGravity(Gravity.TOP, 0, 0);
                    t_top.show();
                    break;
            }
        }
    }

}
