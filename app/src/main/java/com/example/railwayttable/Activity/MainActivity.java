package com.example.railwayttable.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SharedPreferences sharedPreferences, sharedPreferencesNight;
    SharedPreferences.Editor editor;
    SwitchCompat switchMode;
    boolean nightMode;
    Toolbar toolbar;
    private static String currentTheme;
    private boolean exitConfirmed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_main);
        backButton();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        toolbar = findViewById(R.id.toolbar);
        switchMode = findViewById(R.id.switchMode);
        sharedPreferencesNight = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferencesNight.getBoolean("nightMode", false);

        switchMode.setChecked(nightMode);

        switchMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferencesNight.edit();
                editor.putBoolean("nightMode", true);
                nightMode = true;

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferencesNight.edit();
                editor.putBoolean("nightMode", false);
                nightMode = false;

            }

            editor.apply();
        });

        applyNightMode(nightMode);
        navigationView.invalidate();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_hamburger_menu_30);


    }


    private void applyNightMode(boolean nightMode) {
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setThemeOfApp();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setThemeOfApp();
    }
    @Override
    protected void onStart() {
        super.onStart();

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.nav_home) {

                    } else if (itemId == R.id.nav_map) {
                        Intent intent3 = new Intent(MainActivity.this, MapActivity.class);
                        startActivity(intent3);
                    } else if (itemId == R.id.nav_information) {

                        Intent intent1 = new Intent(MainActivity.this, InformationActivity.class);
                        startActivity(intent1);

                    } else if (itemId == R.id.nav_policy) {
                        Intent intent2 = new Intent(MainActivity.this, PolicyActivity.class);
                        startActivity(intent2);

                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
        );

    }








    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void setThemeOfApp(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String selectedTheme = sharedPreferences.getString("color_option", "BLUE");
        currentTheme = selectedTheme;
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

    private void backButton() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }  else {
                    if (exitConfirmed) {
                        exitApp();
                    } else {
                        showExitConfirmationDialog();
                    }
                }
            }
        };

        dispatcher.addCallback(this, callback);
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }  else {
            if (exitConfirmed) {
                exitApp();
            } else {
                showExitConfirmationDialog();
            }
        }
    }
    private void showExitConfirmationDialog() {
        boolean nightMode = sharedPreferencesNight.getBoolean("nightMode", false);
        int dialogStyle = nightMode ? R.style.DialogWindow_Dark : R.style.DialogWindow_Light;

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, dialogStyle));
        builder.setMessage("Czy na pewno chcesz wyjść z aplikacji?")
                .setPositiveButton("Tak", (dialog, which) -> {
                    dialog.dismiss();
                    exitConfirmed = true;
                    exitApp();
                })
                .setNegativeButton("Anuluj", (dialog, which) -> dialog.dismiss())
                .show();
    }
    private void exitApp() {
        sharedPreferences.edit().putString("color_option", currentTheme).apply();
        finishAffinity();
        System.exit(0);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }

        else if (itemId == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void openRoute(View v) {
        Intent intent = new Intent(MainActivity.this, RouteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void openFavorites(View v) {
        Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void openTimetable(View v) {
        Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void openPlanner(View v) {
        Intent intent = new Intent(MainActivity.this, PlannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
