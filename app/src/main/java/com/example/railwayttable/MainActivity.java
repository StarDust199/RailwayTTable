package com.example.railwayttable;


import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

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

public class MainActivity extends AppCompatActivity {
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
        CardView cardRoute=findViewById(R.id.CardRoute);
        CardView cardFavorites=findViewById(R.id.CardFavorites);
        CardView cardPlaner=findViewById(R.id.CardPlanner);
        CardView cardTimetable=findViewById(R.id.CardTimetable);
// Pobierz wybrany motyw
        ThemeManager.Theme selectedTheme = ThemeManager.getSelectedTheme();

// Ustaw odpowiedni kolor dla CardView na podstawie wybranego motywu
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getTheme();

// Ustaw odpowiedni kolor dla CardView na podstawie wybranego motywu
        switch (selectedTheme) {
            case THEME_A:
                cardRoute.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blueb));
                cardFavorites.setCardBackgroundColor(ContextCompat.getColor(this, R.color.almost_white));
                cardPlaner.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_orange));
                cardTimetable.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_blue1));
                break;
            case THEME_B:
                cardRoute.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sunny_orange));
                cardFavorites.setCardBackgroundColor(ContextCompat.getColor(this, R.color.very_light_green));
                cardPlaner.setCardBackgroundColor(ContextCompat.getColor(this, R.color.green));
                cardTimetable.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_green));
                break;
            case THEME_C:
                cardRoute.setCardBackgroundColor(ContextCompat.getColor(this, R.color.rose));
                cardFavorites.setCardBackgroundColor(ContextCompat.getColor(this, R.color.purple));
                cardPlaner.setCardBackgroundColor(ContextCompat.getColor(this, R.color.sea));
                cardTimetable.setCardBackgroundColor(ContextCompat.getColor(this, R.color.fuchsia));
                break;
            default:
                // Domyślny motyw
                cardRoute.setCardBackgroundColor(ContextCompat.getColor(this, R.color.blueb));
                cardFavorites.setCardBackgroundColor(ContextCompat.getColor(this, R.color.almost_white));
                cardPlaner.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_orange));
                cardTimetable.setCardBackgroundColor(ContextCompat.getColor(this, R.color.light_blue1));
                break;
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        View navigationMenuView = navigationView.getChildAt(0);
        if (navigationMenuView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) navigationMenuView;
            recyclerView.setVerticalScrollBarEnabled(true);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icons8_menu_64);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.nav_open, R.string.nav_close);
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

    public void openRoute(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, RouteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
    public void openFavorites(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, FavoritesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }


    public void openTimetable(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, TimetableActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
    public void openPlanner(View v) {
        Intent intent;
        intent = new Intent(MainActivity.this, PlannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
}