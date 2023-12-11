package com.example.railwayttable.Activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

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
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    String godzina;
    DbHelper dbHelper = new DbHelper(this);
    private RecyclerView recyclerViewDestination, recyclerViewStart;
    private String startStation,start;
    private String destinationStation,end;
    private LineDrawingRecyclerViewTouchListener lineDrawingTouchListener;

    public FavoritesActivity() {
    }

    @SuppressLint("ClickableViewAccessibility")
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
        godzina = getCurrentSystemTime();


        recyclerViewStart = findViewById(R.id.recyclerViewStart);
        recyclerViewStart.setLayoutManager(new LinearLayoutManager(this));
        List<StartStationModel> startStationList = dbHelper.getFavoriteStartStations();
        StationAdapter startStationAdapter = new StationAdapter(startStationList, lineDrawingTouchListener);
        recyclerViewStart.setAdapter(startStationAdapter);
        startStationAdapter.notifyDataSetChanged();

        recyclerViewDestination = findViewById(R.id.recyclerViewDestination);
        recyclerViewDestination.setLayoutManager(new LinearLayoutManager(this));
        List<DestinationModel> destinationStationList = dbHelper.getDestinationStations();
        DestinationAdapter destinationStationAdapter = new DestinationAdapter(destinationStationList, lineDrawingTouchListener);
        recyclerViewDestination.setAdapter(destinationStationAdapter);
        lineDrawingTouchListener = new LineDrawingRecyclerViewTouchListener(recyclerViewStart, recyclerViewDestination);


        recyclerViewStart.setOnTouchListener(lineDrawingTouchListener);
        recyclerViewDestination.setOnTouchListener(lineDrawingTouchListener);



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

    private String findNameAtPosition(RecyclerView recyclerView, float x, float y) {
        Log.d("Touch", "x=" + x + ", y=" + y);

        View child = recyclerView.findChildViewUnder(x, y);
        if (child != null) {
            int position = recyclerView.getChildAdapterPosition(child);
            Log.d("Touch", "Position=" + position);
            if (position != RecyclerView.NO_POSITION) {
                RecyclerView.Adapter adapter = recyclerView.getAdapter();
                if (adapter instanceof StationAdapter) {
                    StationAdapter stationAdapter = (StationAdapter) adapter;
                    Station station = stationAdapter.getStationAtPosition(position);
                    if (station != null) {
                        Log.d("Touch", "StationName=" + station.getStationName());
                        return station.getStationName();
                    }
                }
            }
        }
        return null;
    }



    public class LineDrawingRecyclerViewTouchListener implements View.OnTouchListener {
        private RecyclerView recyclerViewStart;
        private RecyclerView recyclerViewDestination;
        private float startX, startY, currentX, currentY;
        private final Path drawingPath;
        private final Paint paint;

        public LineDrawingRecyclerViewTouchListener(RecyclerView recyclerViewStart, RecyclerView recyclerViewDestination) {
            this.recyclerViewStart = recyclerViewStart;
            this.recyclerViewDestination = recyclerViewDestination;
            drawingPath = new Path();

            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("Touch", "ACTION_DOWN: x=" + startX + ", y=" + startY);
                    startX = event.getX();
                    startY = event.getY();
                    drawingPath.moveTo(startX, startY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.d("Touch", "ACTION_MOVE: x=" + event.getX() + ", y=" + event.getY());
                    currentX = event.getX();
                    currentY = event.getY();
                    drawingPath.lineTo(currentX, currentY);
                    recyclerViewStart.invalidate();
                    recyclerViewDestination.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();

                    startStation = String.valueOf(findNameAtPosition(recyclerViewStart, startX, startY));
                    destinationStation = String.valueOf(findNameAtPosition(recyclerViewDestination, endX, endY));
                    Log.d("Touch", "Start Station Name: " + startStation);
                    Log.d("Touch", "Destination Station Name: " + destinationStation);
                    openNewActivity(startStation,destinationStation,godzina);
                    drawingPath.reset();
                    recyclerViewStart.invalidate();
                    recyclerViewDestination.invalidate();
                    break;
            }
            return true;
        }

        public void draw(Canvas canvas) {
            canvas.drawPath(drawingPath, paint);
        }
    }
    private void openNewActivity(String startStation, String destinationStation, String czasTrwania) {

        Intent intent = new Intent(this, TravelActivity.class);
        intent.putExtra("START_STATION", startStation);
        intent.putExtra("END_STATION", destinationStation);
        intent.putExtra("GODZINA", godzina);
        startActivity(intent);
    }


}
