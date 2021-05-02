package com.example.appnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;


public class Database extends SQLiteOpenHelper {

    Context context;
    public static final String DatabaseName = "MyNotes";
    private static final int DatabaseVersion = 1;

    private static final String TableName = "mynotes";
    private static final String ColumnID = "id";
    private static final String ColumnTitle = "title";
    private static final String ColumnDescription = "description";

    public Database(@Nullable Context context) {
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
        long resultVale = db.insert(TableName, null, contentValues);

        if (resultVale == -1) {
            Toast.makeText(context, "Data not added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Data addded", Toast.LENGTH_SHORT).show();
        }
    }

    //Đọc hết database
    public Cursor getAllList(){
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
}
