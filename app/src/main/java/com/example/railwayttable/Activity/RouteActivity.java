package com.example.railwayttable.Activity;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity {



    SharedPreferences sharedPreferences, sharedPreferencesNight;
    Button button;


    EditText datePicker, timePicker, stationA, stationB;
    int year;
    int month;
    int day;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_route);
        backButton();

        AutoCompleteTextView stationA = findViewById(R.id.stacjaA);
        AutoCompleteTextView stationB = findViewById(R.id.stacjaB3);
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);

        database.child("Odjazdy/Stacje posrednie").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()){
                    String suggestion = suggestionSnapshot.getKey();
                    autoComplete.add(suggestion);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        stationA.setAdapter(autoComplete);

        ArrayAdapter<String> adapterB = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        stationB.setAdapter(adapterB);


        button=findViewById(R.id.button_search);
        timePicker = findViewById(R.id.godzina);
        datePicker = findViewById(R.id.czas);


        button.setOnClickListener(v -> {
            String startStation = stationA.getText().toString();
            String endStation = stationB.getText().toString();
        });
        stationA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() >= 3) {
                    getIntermediateStations(charSequence.toString(), autoComplete);

                }
            }


            @Override
            public void afterTextChanged(Editable s) {}
        });

        stationB.setOnTouchListener((view, motionEvent) -> {

            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (stationB.getRight() - stationB.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    String textA = stationA.getText().toString();
                    String textB = stationB.getText().toString();

                    stationA.setText(textB);
                    stationB.setText(textA);

                    return true;
                }

            }
            return false;
        });


        sharedPreferencesNight = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String defaultDate = sdf.format(new Date());
        boolean nightMode = sharedPreferencesNight.getBoolean("nightMode", false);

        datePicker.setText(defaultDate);
        Calendar calendar = Calendar.getInstance();
        datePicker.setOnClickListener(v -> {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            int dialogStyle = nightMode ? R.style.DialogWindowCalendar_Dark : R.style.DialogWindowCalendar_Light;

            DatePickerDialog datePickerDialog = new DatePickerDialog(RouteActivity.this,
                    dialogStyle,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        datePicker.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                    },
                    year, month, day);
            datePicker.invalidate();
            datePickerDialog.show();




        });

        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String currentTime = sdf1.format(new Date());


        timePicker.setText(currentTime);
        timePicker.setOnClickListener(v -> {
            if (!timePicker.isFocused()) {
                openDialog();
            }

        });
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Wyszukaj");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    private void getIntermediateStations(String query, ArrayAdapter<String> adapter) {
        DatabaseReference stacjePosrednieRef = FirebaseDatabase.getInstance().getReference("/Odjazdy/Stacje posrednie");

        stacjePosrednieRef.orderByChild(query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> stationNames = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String nazwaStacji = snapshot.getKey();
                    stationNames.add(nazwaStacji);
                }

                Log.d("TAG", "Pobrane stacje: " + stationNames);

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
        Intent intent = new Intent(RouteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private void openDialog() {
        boolean nightMode = sharedPreferencesNight.getBoolean("nightMode", false);
        String selectedTheme = sharedPreferences.getString("color_option", "BLUE");

        int dialogStyle;
        if (nightMode) {
            switch (selectedTheme) {
                case "VIOLET":
                    dialogStyle = R.style.DialogWindow_Violet;
                    break;
                case "GREEN":
                    dialogStyle = R.style.DialogWindow_Green;
                    break;
                default:
                    dialogStyle = R.style.DialogWindow_Blue;
                    break;
            }
        } else {
            switch (selectedTheme) {
                case "VIOLET":
                    dialogStyle = R.style.DialogWindow_Light_Violet;
                    break;
                case "GREEN":
                    dialogStyle = R.style.DialogWindow_Light_Green;
                    break;
                default:
                    dialogStyle = R.style.DialogWindow_Light_Blue;
                    break;
            }
        }

        TimePickerDialog time = new TimePickerDialog(
                new ContextThemeWrapper(this, dialogStyle),
                dialogStyle,
                (view, hourOfDay, minute) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timePicker.setText(formattedTime);
                },
                15, 0, true
        );
        timePicker.invalidate();
        time.show();
    }
}