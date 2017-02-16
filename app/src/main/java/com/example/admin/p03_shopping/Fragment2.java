package com.example.admin.p03_shopping;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

public class Fragment2 extends Fragment {
    Button buttonFinish;
    GridView gridView;
    DBHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<String> chosenItems;
    ContentValues cv;
    Cursor c;
    String[] itemsArray;
    ArrayAdapter<String> adapter;
    String price;
    int selectedPosition;
    int exp = 0;
    int sum = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment2, null);
        gridView = (GridView) v.findViewById(R.id.gridView);
        return v;
    }
}