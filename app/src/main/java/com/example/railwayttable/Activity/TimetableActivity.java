package com.example.railwayttable.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TimetableActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Spinner spinnerStation;

    Toolbar toolbar;
    DatabaseReference databaseReference;
    CustomArrayAdapter adapter;
    ArrayList<ConnectionModel> list;
    RecyclerView recyclerView;
    ConnectionAdapter connectionAdapter;
    private final Handler handler = new Handler();
    private final int REFRESH_INTERVAL = 60000;
    String selectedStation;
    String godzina = getCurrentSystemTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_timetable);
        backButton();
        toolbar = findViewById(R.id.toolbar2);
        spinnerStation = findViewById(R.id.spinnerStation);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.connectionRecyclerViewTimetable);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        connectionAdapter = new ConnectionAdapter(this, list);
        recyclerView.setAdapter(connectionAdapter);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshList();
                handler.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);

        getAllStations();
        adapter = new CustomArrayAdapter(this, android.R.layout.simple_spinner_item, new ArrayList<>());


        spinnerStation.setAdapter(adapter);


        spinnerStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedStation = (String) parentView.getItemAtPosition(position);



                getAllConnections(selectedStation, adapter);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }
    private void getAllConnections(String selectedStation, CustomArrayAdapter adapter) {
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
                        String trainType = trainSnapshot.child("typ").getValue(String.class);

                        DatabaseReference stacjeRef = trainSnapshot.getRef().child("Stacje");
                        stacjeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot stacjeSnapshot) {
                                boolean startStationFound = false;

                                ConnectionModel tempConnection = new ConnectionModel();

                                for (DataSnapshot stationSnapshot : stacjeSnapshot.getChildren()) {
                                    String stationName = stationSnapshot.getKey();
                                    String departureTime = stationSnapshot.child("odjazd").getValue(String.class);
                                    String arrivalTime = stationSnapshot.child("przyjazd").getValue(String.class);

                                    assert stationName != null;
                                    if (stationName.equals(selectedStation)) {
                                        startStationFound = true;
                                        tempConnection.setGodzinaOdjazdu(departureTime);
                                    }

                                    boolean departureTimeIsAfterRequestedTime = isTimeAfter(departureTime, godzina);



                                    if (startStationFound && departureTimeIsAfterRequestedTime) {

                                        tempConnection.setNazwa(connectionName);
                                        tempConnection.setNumer(trainNumber);
                                        tempConnection.setStacjaKon(trainSnapshot.child("stacja koncowa").getValue(String.class));
                                        tempConnection.setTyp(trainType);

                                        Map<String, Map<String, String>> tempStacje = new HashMap<>();
                                        Map<String, String> tempDetails = new HashMap<>();
                                        tempDetails.put("odjazd", departureTime);
                                        tempDetails.put("przyjazd", arrivalTime);
                                        tempStacje.put(stationName, tempDetails);

                                        tempConnection.setStacje(tempStacje);

                                        if (!selectedStation.equals(tempConnection.getStacjaKon())) {
                                            list.add(tempConnection);
                                        }
                                        break;
                                    }
                                }



                                list.sort(new ConnectionModel.GodzinaOdjazduComparator());
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
    private void refreshList() {




        Iterator<ConnectionModel> iterator = list.iterator();
        while (iterator.hasNext()) {
            ConnectionModel connection = iterator.next();
            String godzinaOdjazdu = connection.getGodzinaOdjazdu();

            if (isTimeAfter(godzina, godzinaOdjazdu)) {
                iterator.remove();
            }
        }


        connectionAdapter.notifyDataSetChanged();
    }

    private boolean isTimeAfter(String time1, String time2) {

        if (time1 != null && time2 != null && !time1.isEmpty() && !time2.isEmpty()) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            try {
                Date date1 = sdf.parse(time1);
                Date date2 = sdf.parse(time2);

                if (date1 != null && date2 != null) {
                    return date1.after(date2);
                } else {
                    return false;
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
    private String getCurrentSystemTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(calendar.getTime());
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