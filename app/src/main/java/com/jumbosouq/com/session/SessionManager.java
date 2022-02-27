package com.jumbosouq.com.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jumbosouq.com.activities.LoginActivity;


public class SessionManager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "NWS_Pref";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String IS_GUEST = "IS_GUEST";


    //username
    public static final String KEY_username = "username";

    //password
    public static final String KEY_password = "password";

    public static final String KEY_batchcount = "batchcount";
    public static final String KEY_VENDOR = "key_vandor";
    public static final String KEY_ADDRESS = "key_address";





    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Create login session
     */
    public void createLoginSession(String username, String password,boolean isGuest) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putBoolean(IS_GUEST, isGuest);
        editor.putString(KEY_username, username);
        editor.putString(KEY_password, password);
        editor.commit();
    }


    public void checkLogin() {
        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }
    }


    /**
     * Clear session details
     */
    public void logoutUser() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }


    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }
    // Get Login State
    public boolean isUserGuest() {
        return pref.getBoolean(IS_GUEST, false);
    }


    public void setBatchcount(String count){
        editor.putString(KEY_batchcount, count);
        editor.commit();
    }
    public void setPrefString(String key,String value){
        editor.putString(key, value);
        editor.commit();
    }
    public void setPrefBoolean(String key,boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }
    public Boolean getPrefBoolean(String key){
        return pref.getBoolean(key, false);
    }
    public String getPrefString(String key){
        return pref.getString(key, "");
    }



    public String getBatchcount(){

        return pref.getString(KEY_batchcount, "");

    }
   public void setVendorId(String count){

        editor.putString(KEY_VENDOR, count);
        editor.commit();


    }


    public void setAddressId(String count){

        editor.putString(KEY_ADDRESS, count);
        editor.commit();


    } public String getAddressId(){
        return pref.getString(KEY_ADDRESS, "");

    }




    public String getVendorid(){

        return pref.getString(KEY_VENDOR, "");

    }


}
