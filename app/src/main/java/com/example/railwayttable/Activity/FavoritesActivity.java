package com.example.railwayttable.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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
import com.example.railwayttable.db.DbHelper;
import com.example.railwayttable.db.DestinationModel;
import com.example.railwayttable.db.StartStationModel;
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
import java.util.List;
import java.util.Map;

public class FavoritesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    private GestureDetector gestureDetector;
    String godzina;
    DbHelper dbHelper = new DbHelper(this);
    private RecyclerView recyclerViewStart,recyclerViewDestination;
    private Station startStation;
    private Station destinationStation;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_favorites);
        gestureDetector = new GestureDetector(this, new MyGestureListener());
        backButton();
        Toolbar toolbar = findViewById(R.id.toolbarFav);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        godzina = getCurrentSystemTime();

        recyclerViewStart = findViewById(R.id.recyclerViewStart);
        recyclerViewStart.setLayoutManager(new LinearLayoutManager(this));
        List<StartStationModel> startStationList = dbHelper.getFavoriteStartStations();
        StationAdapter startStationAdapter = new StationAdapter(startStationList);
        recyclerViewStart.setAdapter(startStationAdapter);
        startStationAdapter.notifyDataSetChanged();

        recyclerViewDestination = findViewById(R.id.recyclerViewDestination);
        recyclerViewDestination.setLayoutManager(new LinearLayoutManager(this));
        List<DestinationModel> destinationStationList = dbHelper.getDestinationStations();
        DestinationAdapter destinationStationAdapter = new DestinationAdapter(destinationStationList);
        recyclerViewDestination.setAdapter(destinationStationAdapter);

        destinationStationAdapter.notifyDataSetChanged();
        recyclerViewStart.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    startStation = findStationAtPosition(recyclerViewStart, event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:

                    destinationStation = findStationAtPosition(recyclerViewStart, event.getX(), event.getY());

                    searchConnection(startStation, destinationStation);
                    break;
            }

            return false;
        });

        recyclerViewDestination.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);


            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    startStation = findStationAtPosition(recyclerViewDestination, event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_UP:

                    destinationStation = findStationAtPosition(recyclerViewDestination, event.getX(), event.getY());

                    searchConnection(startStation, destinationStation);
                    break;
            }

            return false;
        });
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
    private void searchConnection(Station startStation, Station destinationStation) {
        ArrayList<ConnectionModel>  list = new ArrayList<>();
        ConnectionAdapter connectionAdapter = new ConnectionAdapter(this, list);
        databaseReference = FirebaseDatabase.getInstance().getReference("trains");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot trainTypeSnapshot : snapshot.getChildren()) {

                    for (DataSnapshot trainSnapshot : trainTypeSnapshot.getChildren()) {
                        String connectionName = trainSnapshot.child("nazwa").getValue(String.class);
                        Long trainNumber = trainSnapshot.child("numer").getValue(Long.class);
                        String trainType = trainSnapshot.child("typ").getValue(String.class);

                        DatabaseReference stacjeRef = trainSnapshot.getRef().child("Stacje");
                        stacjeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot stacjeSnapshot) {
                                boolean startStationFound = false;
                                boolean endStationFound = false;
                                ConnectionModel tempConnection = new ConnectionModel();

                                for (DataSnapshot stationSnapshot : stacjeSnapshot.getChildren()) {
                                    String stationName = stationSnapshot.getKey();
                                    String departureTime = stationSnapshot.child("odjazd").getValue(String.class);
                                    String arrivalTime = stationSnapshot.child("przyjazd").getValue(String.class);

                                    assert stationName != null;
                                    if (stationName.equals(startStation)) {
                                        startStationFound = true;
                                        tempConnection.setGodzinaOdjazdu(departureTime);
                                    }

                                    if (stationName.equals(destinationStation)) {
                                        endStationFound = true;
                                        tempConnection.setGodzinaPrzyjazdu(arrivalTime);
                                    }

                                    boolean departureTimeIsAfterRequestedTime = isTimeAfter(departureTime, godzina);

                                    if (startStationFound && endStationFound && departureTimeIsAfterRequestedTime) {
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
                                        list.add(tempConnection);
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
    private Station findStationAtPosition(RecyclerView recyclerView, float x, float y) {
        View child = recyclerView.findChildViewUnder(x, y);
        if (child != null) {
            int position = recyclerView.getChildAdapterPosition(child);
            if (position != RecyclerView.NO_POSITION) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof StationAdapter) {
                    StationAdapter stationAdapter = (StationAdapter) adapter;
                    return stationAdapter.getStationAtPosition(position);
                }
            }
        }
        return null;
    }
    private static class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}