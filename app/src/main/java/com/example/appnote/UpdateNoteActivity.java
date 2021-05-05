package com.example.appnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

public class UpdateNoteActivity extends AppCompatActivity {

    EditText title, description;
    String id;
    Button btnUpdateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        btnUpdateNote = findViewById(R.id.btnUpdateNote);

        Intent i = getIntent();     //get Intent from MainActivity
        title.setText(i.getStringExtra("title"));
        description.setText(i.getStringExtra("description"));
        id = i.getStringExtra("id");

        btnUpdateNote.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(title.getText().toString()) || !TextUtils.isEmpty(description.getText().toString())){
                Database db = new Database(UpdateNoteActivity.this);
                db.updateNote(title.getText().toString(), description.getText().toString(), id);
                finish();
            }
        });
    }
}