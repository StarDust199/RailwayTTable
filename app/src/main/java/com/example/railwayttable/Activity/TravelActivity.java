package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.railwayttable.ConnectionAdapter;
import com.example.railwayttable.R;
import com.example.railwayttable.Response.Connection;

import java.util.List;

public class TravelActivity extends AppCompatActivity{
        SharedPreferences sharedPreferences, sharedPreferencesNight;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setThemeOfApp();
            setContentView(R.layout.activity_travel);
            Toolbar toolbar = findViewById(R.id.toolbarTrav);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            List<Connection> connections = getIntent().getParcelableArrayListExtra("connections");
            RecyclerView connectionRecyclerView = findViewById(R.id.connectionRecyclerView);


            ConnectionAdapter adapter = new ConnectionAdapter(connections);
            connectionRecyclerView.setAdapter(adapter);
            connectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        @Override
        public void onBackPressed() {
            Intent intent = new Intent(TravelActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }



        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                Intent intent = new Intent(TravelActivity.this, MainActivity.class);
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

