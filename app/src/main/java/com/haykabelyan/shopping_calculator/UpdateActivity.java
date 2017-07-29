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
    private Button buttonOK, buttonCancel;
    private ArrayList<String> itemsList;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cv;
    private Cursor c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        text = (EditText) findViewById(R.id.editText2);
        buttonOK = (Button) findViewById(R.id.updateOKButton);
        buttonCancel = (Button) findViewById(R.id.updateCancelButton);
        buttonOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.updateOKButton) {
            int count = 0;
            Intent intent = getIntent();
            String oldName = intent.getStringExtra("oldName");
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
                    if (name.equalsIgnoreCase(itemsList.get(i)))
                        count++;
                if (count > 0)
                    Toast.makeText(getApplicationContext(), "Item already exists. Choose another name.", Toast.LENGTH_SHORT).show();
                else {
                    cv.put("name", name);
                    db.update("items", cv, "name = ?", new String[]{oldName});
                    startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Invalid item name", Toast.LENGTH_SHORT).show();
            }
        }
        if (view.getId() == R.id.updateCancelButton)
            startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
    }
}