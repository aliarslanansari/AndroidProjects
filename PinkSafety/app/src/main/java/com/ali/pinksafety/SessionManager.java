package com.ali.pinksafety;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    public static final String KEY_ID_admin = "id_admin";
    public static final String KEY_PASSWORD_admin = "password";
    public static final String KEY_EMAIL_admin = "email";
    private static final String Pref_Name = "Login_admin";
    private static final String FB_TOKEN = "fbtoken";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    Context context;
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public SessionManager(Context context1) {
        this.context = context1;
        pref = context.getSharedPreferences(Pref_Name, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String email, String password) {

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL_admin, email);
        editor.putString(KEY_PASSWORD_admin, password);
        editor.commit();
    }

    public void storeEmergencyCon(String EMER1,String EMER2, String EMER3) {
        editor.putString("EMER1", "8805751865");
        editor.putString("EMER2", "8554027909");
        editor.putString("EMER3", "7350898584");
        editor.commit();
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID_admin, pref.getString(KEY_ID_admin, ""));
        user.put(KEY_PASSWORD_admin, pref.getString(KEY_PASSWORD_admin, ""));
        user.put(KEY_EMAIL_admin, pref.getString(KEY_EMAIL_admin, ""));
        return user;
    }

    public void storeToken(String FbTokenString){
        editor.putString(FB_TOKEN, FbTokenString);
        editor.commit();
    }
    public String getToken(){
        return pref.getString(FB_TOKEN,"");
    }

    public String getEmer(String EMER){
        return pref.getString(EMER,"");
    }

    public void logoutUser() {
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}
