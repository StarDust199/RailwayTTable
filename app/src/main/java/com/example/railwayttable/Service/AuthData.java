package com.example.railwayttable.Service;

import android.util.Base64;

public class AuthData {
    private String username;
    private String password;

    public AuthData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Gettery i settery (opcjonalnie)

    // Przydatna metoda do pobierania nagłówków uwierzytelnienia (np. Basic Auth) dla żądań API
    public String getAuthorizationHeader() {
        String credentials = username + ":" + password;
        String base64Credentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        return "Basic " + base64Credentials;
    }
}
