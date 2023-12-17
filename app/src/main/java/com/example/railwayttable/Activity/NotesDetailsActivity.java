package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.VISIBLE;
import static com.google.android.material.color.utilities.MaterialDynamicColors.error;

public class NotesDetailsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Button buttonSearch;
    ImageButton imageButton;
    EditText txtTitle, txtComment;
    TextView txtDelete;
    ArrayList<ConnectionModel> list;
    String title, comment, docId;
    ConnectionAdapter connectionAdapter;
    Toolbar toolbar;
    String startStation, endStation, godzina, data,description;
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_notes_details);
        backButton();
        buttonSearch = findViewById(R.id.button_search_planner);
        imageButton = findViewById(R.id.saveNoteBtn);
        txtTitle = findViewById(R.id.tytul);
        txtDelete = findViewById(R.id.textDelete);
        txtComment = findViewById(R.id.komentarz);
        toolbar = findViewById(R.id.toolbarAddNotes);
        title = getIntent().getStringExtra("title");
        comment = getIntent().getStringExtra("comment");
        docId = getIntent().getStringExtra("docId");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        startStation = intent.getStringExtra("START_STATION");
        endStation = intent.getStringExtra("END_STATION");
        godzina = intent.getStringExtra("GODZINA");

        data = intent.getStringExtra("DATA");
        connectionAdapter = new ConnectionAdapter(this, list);
        if (docId != null && docId.isEmpty()) {
            isEditMode = true;
        }

        txtTitle.setText(title);
        txtComment.setText(comment);

        if (isEditMode) {
            toolbar.setTitle("Edytuj plan");


        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("trains");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot trainTypeSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot trainSnapshot : trainTypeSnapshot.getChildren()) {
                        String connectionName = trainSnapshot.child("nazwa").getValue(String.class);
                        Long trainNumber = trainSnapshot.child("numer").getValue(Long.class);
                        String trainType = trainSnapshot.child("typ").getValue(String.class);

                        DatabaseReference stacjeRef = trainSnapshot.getRef().child("Stacje");
                        stacjeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot stacjeSnapshot) {
                                boolean startStationFound = false;
                                boolean endStationFound = false;

                                for (DataSnapshot stationSnapshot : stacjeSnapshot.getChildren()) {
                                    String stationName = stationSnapshot.getKey();
                                    String departureTime = stationSnapshot.child("odjazd").getValue(String.class);

                                    assert stationName != null;
                                    if (stationName.equals(startStation)) {
                                        startStationFound = true;
                                    }

                                    if (stationName.equals(endStation)) {
                                        endStationFound = true;
                                    }

                                    boolean departureTimeIsAfterRequestedTime = isTimeAfter(departureTime, godzina);

                                    if (startStationFound && endStationFound && departureTimeIsAfterRequestedTime) {

                                        description = "Nazwa połączenia: " + connectionName +
                                                "\nNumer pociągu: " + trainNumber +
                                                "\nTyp pociągu: " + trainType +
                                                "\nStacja końcowa: " + trainSnapshot.child("stacja koncowa").getValue(String.class) +
                                                "\nGodzina odjazdu: " + departureTime;

                                        txtComment.setText(description);

                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        buttonSearch.setOnClickListener(v -> openRoute());
        imageButton.setOnClickListener(v -> saveNote());
        txtDelete.setOnClickListener(v -> deletePlann());


    }

    public void deletePlann() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReference().document(docId);
        documentReference.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utility.showToast(NotesDetailsActivity.this, "Usunięto pomyslnie");
                backToMain();
            } else {
                Utility.showToast(NotesDetailsActivity.this, "Nie udąło się usunąć");
            }
        });
    }

    public void saveNote() {
        String noteTitle = txtTitle.getText().toString();
        String noteComment = txtComment.getText().toString();
        if (noteTitle.isEmpty() || noteComment.isEmpty()) {
            Toast.makeText(this, "Pole nie może być puste!", Toast.LENGTH_SHORT).show();
        }
        Planner planner = new Planner();
        planner.setTitle(noteTitle);
        planner.setDescription(noteComment);
        planner.setTimestamp(Timestamp.now());

        saveNoteToFirebase(planner);


    }


    private boolean isTimeAfter(String time1, String time2) {

        if (time1 == null || time2 == null) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date1 = sdf.parse(time1);
            Date date2 = sdf.parse(time2);

            return date1 != null && date2 != null && date1.after(date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(NotesDetailsActivity.this, PlannerActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void backToMain() {
        Intent intent = new Intent(NotesDetailsActivity.this, PlannerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
    private void saveNoteToFirebase(Planner planner) {
        DocumentReference documentReference;
        documentReference=Utility.getCollectionReference().document();
        documentReference.set(planner).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                Utility.showToast(NotesDetailsActivity.this, "Dodano pomyślnie");
            } else {
                Utility.showToast(NotesDetailsActivity.this,"Nie udało się dodać");
            }
        });
    }

    public void openRoute() {
        Intent intent = new Intent(this, RouteAcitvitySecond.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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