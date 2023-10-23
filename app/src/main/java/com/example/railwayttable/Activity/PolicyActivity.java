package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;

public class PolicyActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences, sharedPreferencesNight;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_policy);
        backButton();
        textview=(TextView)findViewById(R.id.textPolicy);
        String pars="Lorem ipsum dolor sit amet, consectetur adipiscing elit. In consectetur venenatis ipsum, quis lacinia dui varius vel. Nullam pharetra odio neque, id porta dolor sodales sit amet. Nullam fermentum finibus consectetur. Phasellus at malesuada magna, nec interdum nisl. Integer sagittis iaculis erat, non ultrices lacus consequat id. Curabitur vitae leo massa. Maecenas posuere arcu orci, nec rutrum nisi pellentesque a. Nulla massa nisl, efficitur imperdiet nibh ut, scelerisque ultrices sapien.\n" +
                "\n" +
                "Sed elementum bibendum elit sit amet pellentesque. Cras auctor odio felis, non tempus enim aliquam in. Pellentesque sit amet augue tristique, bibendum odio a, tempor ipsum. Integer et aliquam ex. Duis nulla lectus, pretium at nibh id, hendrerit cursus justo. Ut sit amet molestie tellus. Duis laoreet vestibulum odio, vel pretium turpis eleifend quis. Praesent accumsan viverra leo hendrerit fringilla. Nullam placerat tellus sed libero faucibus, fringilla ultrices nisl vulputate. Ut metus sem, rhoncus vel ultricies vel, dictum in ipsum. Vestibulum pretium varius ligula.";
        textview.setText(pars);
        textview.setMovementMethod(new ScrollingMovementMethod());
        Toolbar toolbar = findViewById(R.id.toolbarPolicy);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
        Intent intent = new Intent(PolicyActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PolicyActivity.this, MainActivity.class);
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