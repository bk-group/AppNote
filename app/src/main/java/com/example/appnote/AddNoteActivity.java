package com.example.appnote;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {

    EditText title, description;
    Button btnAddNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        btnAddNote = findViewById(R.id.btnAddNote);

        btnAddNote.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(title.getText().toString()) || !TextUtils.isEmpty(description.getText().toString())) {
                DatabaseManager.getInstance(this).addNote(new Note(title.getText().toString(), description.getText().toString()));
                finish();
            }

        });
    }
}