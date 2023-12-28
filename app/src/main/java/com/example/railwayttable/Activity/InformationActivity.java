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

public class InformationActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences, sharedPreferencesNight;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_info);
        backButton();
        Toolbar toolbar = findViewById(R.id.toolbarInfo);
        setSupportActionBar(toolbar);
        textview= findViewById(R.id.textInfo);
        String pars=    "OPIS\n" +
                    "Rozklad Jazdy - mobilny rozkład jazdy pociągów\n" +
                    "\n" +
                    "Apliakcja Rozkład Jazdy przeznaczona na urządzenia mobilne oferuje \n" +
                    "informacje na temat rozkzładu jazdy obejmujące przewozników Polregio oraz PKP intercity na trasie Katowice - Wrocław.\n" +
                    "Jest to bezpłatna apliakcja umożliwiająca wyszukanie połączeń a także zawierajaca dodatkowe funkcje, których nie posiadają inne aplikacje.\n" +
                    "\n" +
                    "Sekcja ulubione i wyszukiwanie gestem\n" +
                    "Aplikacja umożliwia dodawanie połączeń do ulubionych, a z poziomu sekcji ulubione wyszukanie informacji o połączeniu za pomocą jedynie gestu bez wpisywania nazw stacji czy godziny\n" +
                    "\n" +
                    "Interaktywny rozkład\n" +
                    "Chcesz sprawdzić połaczenia na dowolnej stacji? Wystarczy, że wybierzesz stację a połączenie zostanie natychmiast wysiwieltone.\n" +
                    "Dodatkowe informacje widoczne są po kliknięciu na połączenie\n" +
                    "\n" +
                    "Zaplanuj wyjazd\n" +
                    "Tworzenie notatek umożliwia planownaie podróży. Chcesz po prostu dokonać wpisu na temat planów? Czy może chcesz od razu dodać informacje na temat połaczenia? Kazda z tych opcji jest możliwa w sekcji planera. Notatki mają możliwość edycji i usunięcia.\n" +
                    "\n" +
                    "Aplikacja stworzona i utrzymywana przez Agnieszkę Hamal.\n" +
                    "email: agnieszka.hamal@gmail.com\n" +
                    "\n" +
                    "Projekt aplikacji jest w całości pomysłem autora, w rozumieniu ustawy z dnia 4 lutego 1994 o prawach autorskich i prawach pokrewnych (Dz. U. 2006 nr 90 poz. 631 ze zm. ) . W przypadku ikon wykorzystano zródła oferujace darmowa licencje - ikony pochodzą głównie ze strony https://icons8.com ";
        textview.setText(pars);
        textview.setMovementMethod(new ScrollingMovementMethod());
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
        Intent intent = new Intent(InformationActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(InformationActivity.this, MainActivity.class);
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