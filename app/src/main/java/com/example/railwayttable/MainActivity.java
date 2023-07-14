package com.example.railwayttable;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private ColorManager colorManager;
    Toolbar toolbar;
    private int toolbarColor;
    private int drawerColor;
    private int cardColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        // Ustawienie ActionBar jako Toolbar
        setSupportActionBar(toolbar);

        // Ustawienie ikony hamburgera jako przycisku nawigacji
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Obsługa kliknięcia elementów w menu nawigacji
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_settings) {
                // Wywołaj aktywność SettingsActivity po kliknięciu na "Settings"
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
            // Obsługa innych elementów menu
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ThemeManager.Theme selectedTheme = ThemeManager.getSelectedTheme();

        switch (selectedTheme) {
            case THEME_A:
                setCardViewColors(R.color.blueb, R.color.almost_white, R.color.light_orange, R.color.light_blue1);
                break;
            case THEME_B:
                setCardViewColors(R.color.sunny_orange, R.color.very_light_green, R.color.green, R.color.light_green);
                break;
            case THEME_C:
                setCardViewColors(R.color.rose, R.color.purple, R.color.sea, R.color.fuchsia);
                break;
            default:
                setCardViewColors(R.color.blueb, R.color.almost_white, R.color.light_orange, R.color.light_blue1);
                break;
        }

        View navigationMenuView = navigationView.getChildAt(0);
        if (navigationMenuView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) navigationMenuView;
            recyclerView.setVerticalScrollBarEnabled(true);
        }

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_menu_64);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icons8_menu_64);
        toggle.setToolbarNavigationClickListener(v -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setCardViewColors(int routeColor, int favoritesColor, int planerColor, int timetableColor) {
        CardView cardRoute = findViewById(R.id.CardRoute);
        CardView cardFavorites = findViewById(R.id.CardFavorites);
        CardView cardPlaner = findViewById(R.id.CardPlanner);
        CardView cardTimetable = findViewById(R.id.CardTimetable);

        cardRoute.setCardBackgroundColor(ContextCompat.getColor(this, routeColor));
        cardFavorites.setCardBackgroundColor(ContextCompat.getColor(this, favoritesColor));
        cardPlaner.setCardBackgroundColor(ContextCompat.getColor(this, planerColor));
        cardTimetable.setCardBackgroundColor(ContextCompat.getColor(this, timetableColor));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    public void OpenSettings(View v) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
