package com.example.railwayttable.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;

public class FavoritesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences, sharedPreferencesNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_favorites);
        backButton();
        Toolbar toolbar = findViewById(R.id.toolbarFav);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }
    private void backButton() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backToMain();
            }
        };

        dispatcher.addCallback(this, callback);
    }

    @Override
    public void onBackPressed() {
     backToMain();

    }

    public void backToMain() {
        Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setThemeOfApp() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String selectedTheme = sharedPreferences.getString("color_option", "BLUE");

        switch (selectedTheme) {
            case "BLUE":
                setTheme(R.style.BlueTheme);
                break;
            case "VIOLET":
                setTheme(R.style.VioletTheme);
                break;
            case "GREEN":
                setTheme(R.style.GreenTheme);
                break;
            default:
                setTheme(R.style.BlueTheme);
                break;
        }
    }
}