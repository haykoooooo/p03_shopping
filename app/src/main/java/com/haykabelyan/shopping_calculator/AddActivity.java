package com.haykabelyan.shopping_calculator;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    private EditText text;
    private Button button;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> itemsList;
    private ContentValues cv;
    private Cursor c;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        text = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button3);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        c = db.query("items", null, null, null, null, null, null);
        itemsList = new ArrayList<String>();
        if (c.moveToFirst())
            do {
                itemsList.add(c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.startAnimation(AnimationUtils.loadAnimation(AddActivity.this, R.anim.clickanim));
                button.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                }, 300);
                int count = 0;
                name = text.getText().toString();
                if (name.length() == 0)
                    Toast.makeText(getApplicationContext(), "No name entered.", Toast.LENGTH_SHORT).show();
                else {
                    cv = new ContentValues();
                    try {
                        if (itemsList.size() > 0) {
                            for (int i = 0; i < itemsList.size(); i++)
                                if (name.equalsIgnoreCase(itemsList.get(i))) {
                                    count++;
                                }
                            if (count > 0)
                                Toast.makeText(getApplicationContext(), "Item already exists. Choose another name.", Toast.LENGTH_SHORT).show();
                            else {
                                itemsList.add(name);
                                cv.put("name", name);
                                db.insert("items", null, cv);
                                Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                                text.setText("");
                            }
                        } else {
                            itemsList.add(name);
                            cv.put("name", name);
                            db.insert("items", null, cv);
                            Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();
                            text.setText("");
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Invalid item name", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}