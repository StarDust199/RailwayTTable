package com.example.railwayttable.Activity;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.example.railwayttable.Response.Connection;
import com.example.railwayttable.Response.TravelStop;
import com.example.railwayttable.Service.ApiInterface;
import com.example.railwayttable.Service.RetrofitClient;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.railwayttable.Service.RetrofitClient.BASE_URL;


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
        AutoCompleteTextView stationA = findViewById(R.id.stacjaA);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        stationA.setAdapter(adapter);
        stationB = findViewById(R.id.stacjaB3);
        button=findViewById(R.id.button_search);
        timePicker = findViewById(R.id.godzina);
        datePicker = findViewById(R.id.czas);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();

                        String credentials = Credentials.basic("ahamal_demo", "WxWdFCmtqLq2@Hi");
                        Request request = original.newBuilder()
                                .header("Authorization", credentials)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                })
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startStation = stationA.getText().toString();
                String endStation = stationB.getText().toString();
                ApiInterface apiInterface = RetrofitClient.getApiInterface();
                Call<List<Connection>> call = apiInterface.getTrip(startStation, endStation);

                call.enqueue(new Callback<List<Connection>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Connection>> call, @NonNull Response<List<Connection>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Connection> connections = response.body();
                            Intent intent = new Intent(RouteActivity.this, TravelActivity.class);
                            intent.putParcelableArrayListExtra("connections", (ArrayList<? extends Parcelable>) connections);
                            startActivity(intent);

                        } else {
                            Toast.makeText(RouteActivity.this, "Wystapił problem", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Connection>> call, @NonNull Throwable t) {

                    }
                });
            }
        });
        stationA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                if (input.length() >= 3) {
                    ApiInterface apiInterface = RetrofitClient.getApiInterface();
                    Call<List<TravelStop>> call = apiInterface.getStation(input);
                    Log.d("TravelStop", "Before making the API call");
                    call.enqueue(new Callback<List<TravelStop>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<TravelStop>> call, @NonNull Response<List<TravelStop>> response) {
                            int statusCode = response.code();
                            if (response.isSuccessful() && response.body() != null) {
                                List<TravelStop> matchingStops = response.body();
                                Log.d("TravelStop", "Matching stops size: " + matchingStops.size());
                                if (matchingStops.isEmpty()) {
                                    Toast.makeText(RouteActivity.this, "Nie znaleziono stacji", Toast.LENGTH_SHORT).show();
                                } else {
                                    List<String> stopNames = new ArrayList<>();
                                    for (TravelStop stop : matchingStops) {
                                        stopNames.add(stop.getTravelStopName());
                                        Log.d("TravelStop", "Stop name: " + stop.getTravelStopName());
                                    }

                                    ArrayAdapter<String> updatedAdapter = new ArrayAdapter<>(RouteActivity.this, android.R.layout.simple_dropdown_item_1line, stopNames);
                                    stationA.setAdapter(updatedAdapter);
                                    updatedAdapter.notifyDataSetChanged();
                                }

                            }else {
                                Toast.makeText(RouteActivity.this, "Błąd z kodem: " + statusCode, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<TravelStop>> call, @NonNull Throwable t) {
                            Log.e("TravelStop", "Error: " + t.getMessage());
                            Toast.makeText(RouteActivity.this, "Wystąpił problem", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Log.d("TravelStop", "After enqueue");
                } else {


                    Toast.makeText(RouteActivity.this, "Wprowadź co najmniej 3 znaki", Toast.LENGTH_SHORT).show();
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