package com.example.railwayttable.Activity;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class RouteActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText datePicker, timePicker;
    int year;
    int month;
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_route);
        timePicker = findViewById(R.id.godzina);
        datePicker = findViewById(R.id.czas);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());
        String defaultDate = sdf.format(new Date());

        datePicker.setText(defaultDate);
        Calendar calendar = Calendar.getInstance();
        datePicker.setOnClickListener(v -> {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(RouteActivity.this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                datePicker.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
            }, year, month, day);
            datePickerDialog.show();
        });

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timePicker.isFocused()) {
                    openDialog();
                }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(RouteActivity.this, MainActivity.class);
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RouteActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void openDialog() {
        TimePickerDialog time = new TimePickerDialog(
                RouteActivity.this,
                0,
                (view, hourOfDay, minute) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    timePicker.setText(formattedTime);
                },
                15, 0, true
        );

        time.show();
    }
}