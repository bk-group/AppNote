package com.example.appnote;

import java.util.ArrayList;

public interface OnDatabaseChangeListener {
    void onDataBaseChanged(ArrayList<Note> notes);
}
