package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TimetableActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Spinner spinnerStation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_timetable);
        backButton();
        Toolbar toolbar = findViewById(R.id.toolbar2);
        spinnerStation = findViewById(R.id.spinnerStation);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getAllStations();
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>());


        spinnerStation.setAdapter(adapter);


        spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String selectedStation = (String) parentView.getItemAtPosition(position);


                getAllConnections(selectedStation, adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    private void getAllConnections(String selectedStation, CustomArrayAdapter adapter) {
        DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("trains");

        trainsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> stationNamesSet = new HashSet<>();
                final boolean[] found = {false};

                for (DataSnapshot trainTypeSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot trainSnapshot : trainTypeSnapshot.getChildren()) {
                        String trainKey = trainSnapshot.getKey();
                        DatabaseReference stationsRef = FirebaseDatabase.getInstance().getReference("/trains/" + trainTypeSnapshot.getKey() + "/" + trainKey + "/Stacje");

                        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot stationSnapshot : snapshot.getChildren()) {
                                    String nazwaStacji = stationSnapshot.getKey();
                                    assert nazwaStacji != null;
                                    if (nazwaStacji.equals(selectedStation)) {
                                        stationNamesSet.add(nazwaStacji);
                                        found[0] = true;
                                    }
                                }

                                List<String> stationNames = new ArrayList<>(stationNamesSet);

                                if (!found[0]) {
                                    stationNames.add("Nie znaleziono stacji");
                                }

                                adapter.clear();
                                adapter.addAll(stationNames);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                databaseError.toException().printStackTrace();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });
    }
    private void getAllStations() {
        DatabaseReference trainsRef = FirebaseDatabase.getInstance().getReference("trains");

        trainsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> stationNamesSet = new HashSet<>();

                for (DataSnapshot trainTypeSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot trainSnapshot : trainTypeSnapshot.getChildren()) {
                        String trainKey = trainSnapshot.getKey();
                        DatabaseReference stationsRef = FirebaseDatabase.getInstance().getReference("/trains/" + trainTypeSnapshot.getKey() + "/" + trainKey + "/Stacje");

                        stationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot stationSnapshot : snapshot.getChildren()) {
                                    String nazwaStacji = stationSnapshot.getKey();
                                    assert nazwaStacji != null;
                                    stationNamesSet.add(nazwaStacji);
                                }

                                List<String> stationNames = new ArrayList<>(stationNamesSet);

                                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, stationNames);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                spinnerStation.setAdapter(spinnerAdapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(TimetableActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        Intent intent = new Intent(TimetableActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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