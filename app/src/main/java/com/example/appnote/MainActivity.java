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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnHandleClickListener, OnDatabaseChangeListener {
    RecyclerView recyclerView;
    FloatingActionButton fab;
    Adapter adapter;
    List<Note> notesList = new ArrayList<>();
    DatabaseHelper databaseHelper;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(intent);
        });

//        notesList = new ArrayList<>();
        coordinatorLayout = findViewById(R.id.layout_main);

        DatabaseManager.getInstance(this).addDatabaseChangeListener(this);


        /*database = new Database(this);
        fetchAllNoteFromDatabase();*/

        /*recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notesList, this::handClick);
        recyclerView.setAdapter(adapter);*/

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void initAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, notesList, this::handClick);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        notesList.clear();
//        databaseHelper = new DatabaseHelper(this);
//        fetchAllNoteFromDatabase();
//        adapter.notifyDataSetChanged();
        DatabaseManager.getInstance(this).refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseManager.getInstance(this).removeDatabaseChangeListener(this::onDataBaseChanged);
    }

    /*private void fetchAllNoteFromDatabase() {
        Cursor cursor = databaseHelper.getAllList();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                notesList.add(new Note(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
            }
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Notes Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

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
        DatabaseHelper db = new DatabaseHelper(MainActivity.this);
        db.deleteAllNote();
        notesList.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void handClick(Note note) {
        Intent intent = new Intent(this, UpdateNoteActivity.class);
        intent.putExtra("title", note.getTitle());
        intent.putExtra("description", note.getDescription());
        intent.putExtra("id", note.getId());
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getBindingAdapterPosition();
            Note item = adapter.getList().get(position);

            adapter.removeItem(position);


            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Item deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.unDo(item, position);
                            recyclerView.scrollToPosition(position);
                        }
                    }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);

                            /*if (!(event==DISMISS_EVENT_ACTION)){
                                DatabaseHelper db = new DatabaseHelper(MainActivity.this);
                                db.deleteSingleItem(item.getId());
                            }*/
                        }
                    });
            snackbar.show();
        }
    };

    @Override
    public void onDataBaseChanged(ArrayList<Note> notes) {
        if (adapter != null) {
            notesList.clear();
            notesList.addAll(notes);
            adapter.notifyDataSetChanged();
        } else  {
            initAdapter();
        }
    }
}