package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TravelActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences, sharedPreferencesNight;
    int positionInserted;
    DatabaseReference databaseReference;
    ConnectionAdapter connectionAdapter;
    ArrayList<ConnectionModel> list;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setThemeOfApp();
            setContentView(R.layout.activity_travel);
            backButton();
            Toolbar toolbar = findViewById(R.id.toolbarTrav);

            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            Intent intent = getIntent();
            String startStation = intent.getStringExtra("START_STATION");
            String endStation = intent.getStringExtra("END_STATION");
            String godzina = intent.getStringExtra("GODZINA");
            TextView textViewStartStation = findViewById(R.id.textStationA);
            TextView textViewEndStation = findViewById(R.id.textStationB);
            textViewStartStation.setText(startStation);
            textViewEndStation.setText(endStation);

            RecyclerView recyclerView = findViewById(R.id.connectionRecyclerView);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            list = new ArrayList<>();
            connectionAdapter = new ConnectionAdapter(this, list);
            recyclerView.setAdapter(connectionAdapter);

            databaseReference = FirebaseDatabase.getInstance().getReference("trains");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    list.clear();

                    for (DataSnapshot trainTypeSnapshot : snapshot.getChildren()) {
                        String trainTypeKey = trainTypeSnapshot.getKey();

                        for (DataSnapshot trainSnapshot : trainTypeSnapshot.getChildren()) {
                            String connectionName = trainSnapshot.child("nazwa").getValue(String.class);
                            Long trainNumber = trainSnapshot.child("numer").getValue(Long.class);

                            DatabaseReference stacjeRef = trainSnapshot.getRef().child("Stacje");
                            stacjeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot stacjeSnapshot) {
                                    boolean startStationFound = false;
                                    boolean endStationFound = false;

                                    for (DataSnapshot stationSnapshot : stacjeSnapshot.getChildren()) {
                                        String stationName = stationSnapshot.getKey();
                                        String departureTime = stationSnapshot.child("odjazd").getValue(String.class);
                                        String arrivalTime = stationSnapshot.child("przyjazd").getValue(String.class);

                                        if (stationName.equals(startStation)) {
                                            startStationFound = true;
                                        }

                                        if (stationName.equals(endStation)) {
                                            endStationFound = true;
                                        }

                                        boolean departureTimeIsAfterRequestedTime = isTimeAfter(departureTime, godzina);

                                        if (startStationFound && endStationFound && departureTimeIsAfterRequestedTime) {
                                            ConnectionModel tempConnection = new ConnectionModel();
                                            tempConnection.setNazwa(connectionName);
                                            tempConnection.setNumer(trainNumber);
                                            tempConnection.setStacjaKon(trainSnapshot.child("stacja koncowa").getValue(String.class));
                                            tempConnection.setTyp(trainTypeKey);

                                            Map<String, Map<String, String>> tempStacje = new HashMap<>();
                                            Map<String, String> tempDetails = new HashMap<>();
                                            tempDetails.put("odjazd", departureTime);
                                            tempDetails.put("przyjazd", arrivalTime);
                                            tempStacje.put(stationName, tempDetails);

                                            tempConnection.setStacje(tempStacje);
                                            list.add(tempConnection);
                                            break;
                                        }
                                    }

                                    connectionAdapter.notifyDataSetChanged();
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
            private boolean isTimeAfter(String time1, String time2) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    Date date1 = sdf.parse(time1);
                    Date date2 = sdf.parse(time2);

                    assert date1 != null;
                    return date1.after(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return false;
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
        Intent intent = new Intent(TravelActivity.this, RouteActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(TravelActivity.this, RouteActivity.class);
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

