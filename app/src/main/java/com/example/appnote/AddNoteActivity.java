package com.example.appnote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {

    EditText title, description;
    Button addNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        addNote = findViewById(R.id.btnAddNote);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database db = new Database(AddNoteActivity.this);
                db.addNote(title.getText().toString(), description.getText().toString());

                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);       //Xoá task, không thể hiện lại AddNoteActivity nếu ta ấn back
                startActivity(intent);
            }
        });
    }
}