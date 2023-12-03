package com.example.railwayttable.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TravelActivity extends AppCompatActivity{
    SharedPreferences sharedPreferences;
    DatabaseReference databaseReference;
    ConnectionAdapter connectionAdapter;
    DbHelper dbHelper=new DbHelper(this);
    private int counter = 0;
    String startStation, endStation;
    ImageView imageFavorite;
    TextView  txtHour,textViewStartStation, textViewEndStation;
    private boolean isFavorite = false;
    ArrayList<ConnectionModel> list;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setThemeOfApp();
            setContentView(R.layout.activity_travel);
            backButton();
            Toolbar toolbar = findViewById(R.id.toolbarTrav);
            imageFavorite = findViewById(R.id.imageFavorite);


            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            Intent intent = getIntent();
            startStation = intent.getStringExtra("START_STATION");
            endStation = intent.getStringExtra("END_STATION");
            String godzina = intent.getStringExtra("GODZINA");
            String data=intent.getStringExtra("DATA");
            txtHour=findViewById(R.id.textHour);
            textViewStartStation = findViewById(R.id.textStationA);
            textViewEndStation = findViewById(R.id.textStationB);
            textViewStartStation.setText(startStation);
            textViewEndStation.setText(endStation);
            imageFavorite.setOnClickListener(v -> {
                changeStarImage();

                String stationStartName = textViewStartStation.getText().toString();
                String stationEndName = textViewEndStation.getText().toString();


                StartStationModel startStationModel = new StartStationModel(counter++,stationStartName);
                DestinationModel destinationModel = new DestinationModel(counter++,stationEndName);


                boolean addedToStart = dbHelper.addStartowa(startStationModel);
                boolean addedToEnd = dbHelper.addKoncowa(destinationModel);

                if (addedToStart && addedToEnd) {
                    showToast("Dodano do ulubionych");
                } else {
                    showToast("Błąd podczas dodawania do ulubionych");
                }
            });
            String combinedText = data + ", " + godzina;
            txtHour.setText(combinedText);

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

                                        if (stationName.equals(endStation)) {
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
    private void changeStarImage() {
        if (isFavorite) {

            imageFavorite.setImageResource(android.R.drawable.btn_star_big_off);
        } else {

            imageFavorite.setImageResource(android.R.drawable.btn_star_big_on);
        }


        isFavorite = !isFavorite;
    }

    private void showToast(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isTimeAfter(String time1, String time2) {

        if (time1 != null && time2 != null) {
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
            }
        } else {

            return false;
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
        intent.putExtra("START_STATION", startStation);
        intent.putExtra("END_STATION", endStation);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(TravelActivity.this, RouteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("START_STATION", startStation);
                intent.putExtra("END_STATION", endStation);
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

