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
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.railwayttable.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;


public class RouteActivity extends AppCompatActivity {



    SharedPreferences sharedPreferences, sharedPreferencesNight;
    Button button;
    private String uuid;
    private String username;
    private String password;

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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        stationA.setAdapter(adapter);
        stationB = findViewById(R.id.stacjaB3);
        button=findViewById(R.id.button_search);
        timePicker = findViewById(R.id.godzina);
        datePicker = findViewById(R.id.czas);
        uuid = UUID.randomUUID().toString();
        username = "ahamal_demo";
        password = "WxWdFCmtqLq2@Hi";

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startStation = stationA.getText().toString();
                String endStation = stationB.getText().toString();
            }
        });
        stationA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() >= 3) {
                    getStation(charSequence.toString(), adapter);
                }
            }


            @Override
            public void afterTextChanged(Editable s) {}
        });

        stationB.setOnTouchListener((view, motionEvent) -> {

            final int DRAWABLE_RIGHT = 2;
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (motionEvent.getRawX() >= (stationB.getRight() - stationB.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                    reversePlaces();
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

    public void reversePlaces() {
        String textA = stationA.getText().toString();
        String textB3 = stationB.getText().toString();

        stationA.setText(textB3);
        stationB.setText(textA);
    }

    private void getStation(String query, ArrayAdapter<String> adapter) {
        String url = "https://wbnet-demo.pkpik.pl:444/admapi/search/travelstops/name";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("query", query);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray stations = response.getJSONArray("stations");

                            List<String> stationNames = new ArrayList<>();
                            for (int i = 0; i < stations.length(); i++) {
                                stationNames.add(stations.getString(i));
                            }

                            adapter.clear();
                            adapter.addAll(stationNames);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String credentials = username + ":" + password;
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("X-correlation-ID", uuid);
                headers.put("Authentication", auth);
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
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