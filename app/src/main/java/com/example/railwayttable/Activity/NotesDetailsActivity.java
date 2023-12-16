package com.example.railwayttable.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.railwayttable.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Button buttonSearch;
    ImageButton imageButton;
    EditText txtTitle, txtComment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setThemeOfApp();
        setContentView(R.layout.activity_notes_details);
        buttonSearch=findViewById(R.id.button_search_planner);
        imageButton=findViewById(R.id.saveNoteBtn);
        txtTitle=findViewById(R.id.tytul);
        txtComment=findViewById(R.id.komentarz);

        buttonSearch.setOnClickListener(v -> openRoute());
        imageButton.setOnClickListener(v -> saveNote() );


    }
    public void saveNote(){
        String noteTitle=txtTitle.getText().toString();
        String noteComment= txtComment.getText().toString();
        if(noteTitle.isEmpty()||noteComment.isEmpty()){
            Toast.makeText(this, "Pole nie może być puste!", Toast.LENGTH_SHORT).show();
        }
        Planner planner=new Planner();
        planner.setTitle(noteTitle);
        planner.setDescription(noteComment);
        planner.setTimestamp(Timestamp.now());
        
        saveNoteToFirebase(planner);

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
        Intent intent = new Intent(this, RouteActivity.class);
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