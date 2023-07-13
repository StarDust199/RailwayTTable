package com.example.railwayttable;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.navigation.NavigationView;

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

        NavigationView navigationView = findViewById(R.id.nav_view);
        View navigationMenuView = navigationView.getChildAt(0);
        if (navigationMenuView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) navigationMenuView;
            recyclerView.setVerticalScrollBarEnabled(true);
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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