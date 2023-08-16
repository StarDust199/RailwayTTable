package com.example.railwayttable;

import android.app.Application;

import com.example.railwayttable.Service.AuthData;

public class MyApplication extends Application {
    private static AuthData authData;

    @Override
    public void onCreate() {
        super.onCreate();
        // Tutaj możesz ustawić dane uwierzytelniające - nazwę użytkownika i hasło
        String username = "ahamal_demo";
        String password = "WxWdFCmtqLq2@Hi";
        authData = new AuthData(username, password);
    }

    public static AuthData getAuthData() {
        return authData;
    }
}
