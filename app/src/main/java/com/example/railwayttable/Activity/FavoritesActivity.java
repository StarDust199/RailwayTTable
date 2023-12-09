package com.example.railwayttable.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;
import com.example.railwayttable.db.DbHelper;
import com.example.railwayttable.db.DestinationModel;
import com.example.railwayttable.db.StartStationModel;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DbHelper dbHelper = new DbHelper(this);
    private List<StartStationModel> startStationList;

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
        RecyclerView recyclerViewStart = findViewById(R.id.recyclerViewStart);
        recyclerViewStart.setLayoutManager(new LinearLayoutManager(this));

        StationAdapter startStationAdapter = new StationAdapter(startStationList);
        recyclerViewStart.setAdapter(startStationAdapter);

        RecyclerView recyclerViewDestination = findViewById(R.id.recyclerViewDestination);
        recyclerViewDestination.setLayoutManager(new LinearLayoutManager(this));
        List<DestinationModel> destinationStationList = dbHelper.getDestinationStations();
        StationAdapter destinationStationAdapter = new StationAdapter(destinationStationList);
        recyclerViewDestination.setAdapter(destinationStationAdapter);
        startStationAdapter.notifyDataSetChanged();
        destinationStationAdapter.notifyDataSetChanged();
        List<StartStationModel> startStations = dbHelper.getAllStartStations();
        for (StartStationModel startStation : startStations) {
            Log.d("TABLE_START_STATIONS", "ID: " + startStation.getId() + ", Stacja: " + startStation.getStacjaPocz() + ", Ulubiona: " + startStation.isFavorite());
        }
        List<DestinationModel> destinationStations = dbHelper.getAllDestinationStations();
        for (DestinationModel destinationStation : destinationStations) {
            Log.d("TABLE_DESTINATION_STATIONS", "ID: " + destinationStation.getId() + ", Stacja: " + destinationStation.getStacjaKon() + ", Ulubiona: " + destinationStation.isFavorite());
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