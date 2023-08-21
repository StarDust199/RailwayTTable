package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int SETTINGS_CODE = 234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);
        setThemeOfApp();
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true); getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


    }


    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return true; }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("color_option")) {
            String selectedTheme = sharedPreferences.getString("color_option", "DEFAULT");
            Log.d("MyApp", "Wartość color_option: " + selectedTheme);


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("selected_theme", selectedTheme);
            startActivity(intent);
            finish();
        }
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
    private void setThemeOfApp(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String selectedTheme = sharedPreferences.getString("color_option", "BLUE");

        if (selectedTheme.equals("BLUE")){
            setTheme(R.style.BlueTheme);
        } else if (selectedTheme.equals("VIOLET")) {
            setTheme(R.style.VioletTheme);
        } else if (selectedTheme.equals("GREEN")) {
            setTheme(R.style.GreenTheme);
        }else{
            setTheme(R.style.BlueTheme);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE && resultCode == RESULT_OK) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
            }



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}