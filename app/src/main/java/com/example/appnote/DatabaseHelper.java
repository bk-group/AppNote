package com.example.appnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    public static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;

    public static final String TableName = "mynotes";
    public static final String ColumnID = "id";
    public static final String ColumnTitle = "title";
    public static final String ColumnDescription = "description";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TableName +
                " (" + ColumnID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ColumnTitle + " TEXT, " +
                ColumnDescription + " TEXT);";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(db);
    }

    public void addNote(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Dùng ContentValues để đưa data vào database
        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnTitle, title);
        contentValues.put(ColumnDescription, description);

        //Thêm data vào database bằng lệnh Insert
        long resultValue = db.insert(TableName, null, contentValues);

        if (resultValue == -1) {
            Toast.makeText(context, "Data not added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data addded", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateNote(String title, String description, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ColumnTitle, title);
        contentValues.put(ColumnDescription, description);

        long resultValue = db.update(TableName, contentValues, "id=?", new String[]{id});

        if (resultValue == -1) {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
        }
    }

    //Đọc hết database
    public Cursor getAllList() {
        String query = "SELECT * FROM " + TableName;
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = null;
        if (database != null) {
            cursor = database.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteAllNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TableName;
        db.execSQL(query);
    }


    public void deleteSingleItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.delete(TableName, "id=?", new String[]{id});
        if (result == -1) {
            Toast.makeText(context, "Item not deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
