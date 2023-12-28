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
        textview= findViewById(R.id.textPolicy);
        String pars=    "POLITYKA PRYWATNOŚCI\n" +
                    "\n" +
                    "1. Aplikacja Rzokład Jazdy jest administrowana przez twórcę aplikacji tj. Agnieszkę Hamal.\n" +
                    "\n" +
                    "2. Zródłem danych wykorzystywanych przez aplikację jest baza dnaych utworzona na podstawie tablicowego rozkładu jazdy. Obecnie w aplikacje nie przetwarzane dane analityczne oraz osobowe." +
                    "Baza dnaych podlega ochronie w rozumieniu ustawy z dnia 27 lipca 2001 o ochronie baz danych (Dz. U. 2001 nr 128 poz. 1402 ze zm.)\n" +
                    "\n" +
                    "3. W przypadku rozwoju apliakcji dane bedą zebrane w celu personalizacji lub świadczenia wybranych usług.\n" +
                    "\n" +
                    "4. Podstawą prawną dotycząca przetwarzania danych jest RODO tj. Rozporządzenie Parlamentu Europejskiego i Rady (UE) w sprawie ochrony danych osobowych.\n" +
                    "\n" +
                    "5. Dane osobowe bedą przechowywane od momentu udostępnienia zgody do momentu jej wycofania.\n" +
                    "\n" +
                    "6. Użytkownikowi przysługuje możliwość zmmiany w udostępnianych danych a także wycofanie zgód.\n" +
                    "\n" +
                    "7. Autor zastrzega sobie możliwosć zmian w polityce prywatnosci, użytkownik bedzie poinformowany o tym w stosowny sposób.";
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