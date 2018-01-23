package com.haykabelyan.shopping_calculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text;
    private Button buttonOK;
    private ArrayList<String> itemsList;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cv;
    private Cursor c;
    private Intent intent;
    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        intent = getIntent();
        oldName = intent.getStringExtra("oldName");
        text = (EditText) findViewById(R.id.editText2);
        text.setText(oldName);
        text.setSelection(text.getText().length());
        buttonOK = (Button) findViewById(R.id.updateOKButton);
        buttonOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int count = 0;
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        itemsList = new ArrayList<String>();
        String name = text.getText().toString();
        c = db.query("items", null, null, null, null, null, null);
        if (c.moveToFirst())
            do {
                itemsList.add(c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        try {
            cv = new ContentValues();
            for (int i = 0; i < itemsList.size(); i++)
                if (name.equalsIgnoreCase(itemsList.get(i)) && !(name.equalsIgnoreCase(oldName)))
                    count++;
            if (count > 0)
                Toast.makeText(getApplicationContext(), "Item already exists. Choose another name.", Toast.LENGTH_SHORT).show();
            else {
                cv.put("name", name);
                db.update("items", cv, "name = ?", new String[]{oldName});
                startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Invalid item name", Toast.LENGTH_SHORT).show();
        }
    }
}