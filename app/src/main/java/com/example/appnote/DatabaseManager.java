package com.example.appnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.appnote.DatabaseHelper.ColumnDescription;
import static com.example.appnote.DatabaseHelper.ColumnID;
import static com.example.appnote.DatabaseHelper.ColumnTitle;
import static com.example.appnote.DatabaseHelper.TableName;

public class DatabaseManager {

    public static DatabaseManager manager;
    private final ArrayList<OnDatabaseChangeListener> mListeners = new ArrayList<>();
    private final SQLiteDatabase db;
    private ArrayList<Note> noteList = new ArrayList<>();

    private GetAllNoteTask task;

    private DatabaseManager(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        refresh();
    }

    public static DatabaseManager getInstance(Context context) {
        if (manager == null) {
            manager = new DatabaseManager(context);
        }
        return manager;
    }

    public void refresh() {
        if (task != null)
            task.cancel(true);

        task = new GetAllNoteTask(this);
        task.execute();
    }

    public void addDatabaseChangeListener(OnDatabaseChangeListener listener) {
        synchronized (mListeners) {
            if (listener != null) {
                if (!mListeners.contains(listener))
                    mListeners.add(listener);
                onDataChanged();
            }
        }
    }

    public void removeDatabaseChangeListener(OnDatabaseChangeListener listener) {
        synchronized (mListeners) {
            mListeners.remove(listener);
        }
    }

    public void addNote(Note note) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnTitle, note.getTitle());
        contentValues.put(ColumnDescription, note.getDescription());

        note.setId((int) db.insert(TableName, null, contentValues));
        noteList.add(note);
        onDataChanged();
    }

    private void onDataChanged() {
        synchronized (mListeners) {
            for (OnDatabaseChangeListener listener : mListeners) {
                if (listener != null) {
                    listener.onDataBaseChanged(noteList);
                }
            }
        }
    }

    public static class GetAllNoteTask extends AsyncTask<Void, Void, ArrayList<Note>> {

        private WeakReference<DatabaseManager> mManagerRef;

        public GetAllNoteTask(DatabaseManager manager) {
            mManagerRef = new WeakReference<>(manager);
        }

        @Override
        protected ArrayList<Note> doInBackground(Void... voids) {
            ArrayList<Note> notes = new ArrayList<>();
            DatabaseManager manager = mManagerRef.get();
            if (manager != null) {
                SQLiteDatabase db = manager.db;
                String query = "SELECT * FROM " + TableName;
                Cursor cursor = db.rawQuery(query, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            try {
                                Note note = new Note();
                                note.setId(cursor.getInt(cursor.getColumnIndex(ColumnID)));
                                note.setTitle(cursor.getString(cursor.getColumnIndex(ColumnTitle)));
                                note.setDescription(cursor.getString(cursor.getColumnIndex(ColumnDescription)));
                                notes.add(note);
                            } catch (SQLException e) {
                                // get error
                            }
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
            }
            return notes;
        }

        @Override
        protected void onPostExecute(ArrayList<Note> notes) {
            DatabaseManager manager = mManagerRef.get();
            manager.noteList = notes;
            manager.onDataChanged();
        }
    }
}
