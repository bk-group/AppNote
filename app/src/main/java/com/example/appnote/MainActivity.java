package com.example.appnote;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Note> notesList = new ArrayList<>();
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //hl

        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

        database = new Database(this);
        fetchAllNoteFromDatabase();

//        notesList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, MainActivity.this, notesList);
        recyclerView.setAdapter(adapter);

    }

    private void fetchAllNoteFromDatabase() {
        Cursor cursor = database.getAllList();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
//                String title = cursor.getString(1);
//                String description = cursor.getString(2);
//                String id = cursor.getString(0);
                notesList.add(new Note(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_note) {
            deleteAllNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllNote() {
        Database db = new Database(MainActivity.this);
        db.deleteAllNote();
        recreate();
    }
}