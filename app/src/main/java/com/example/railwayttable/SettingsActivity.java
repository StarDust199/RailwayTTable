package com.example.railwayttable;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    RadioButton rd1, rd2, rd3;
    int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        rd1 = (RadioButton) findViewById(R.id.radioButtonThemeA);
        rd2 = (RadioButton) findViewById(R.id.radioButtonThemeB);
        rd3 = (RadioButton) findViewById(R.id.radioButtonThemeC);
        radioGroup=findViewById(R.id.radioGroup);

        rd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd2.setChecked(false);
                rd3.setChecked(false);
                type = 1;
            }
        });

        rd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(false);
                rd3.setChecked(false);
                type = 2;
            }
        });

        rd3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rd1.setChecked(false);
                rd2.setChecked(false);
                type = 3;
            }
        });


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (type == 1) {
                setTheme(R.style.ThemeA);
                recreate();
            } else if (type == 2) {
                setTheme(R.style.ThemeB);
                recreate();
            } else if (type == 3) {
                setTheme(R.style.ThemeC);
                recreate();
            }
        });

    }




}
