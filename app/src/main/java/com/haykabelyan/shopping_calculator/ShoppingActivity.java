package com.haykabelyan.shopping_calculator;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity {

    ListView listView;
    Button buttonNext;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> itemsList;
    ArrayList<String> chosenItems;
    private ContentValues cv;
    private Cursor c;
    private String[] itemsArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        c = db.query("items", null, null, null, null, null, null);
        itemsList = new ArrayList<String>();
        if (c.moveToFirst())
            do {
                itemsList.add(c.getString(c.getColumnIndex("name")));
            } while (c.moveToNext());
        if (itemsList.size() > 0) {
            setContentView(R.layout.shopping_list_activity);
            buttonNext = (Button) findViewById(R.id.buttonNext);
            itemsArray = new String[itemsList.size()];
            for (int i = 0; i < itemsList.size(); i++) {
                itemsArray[i] = itemsList.get(i);
            }
            listView = (ListView) findViewById(R.id.listView2);
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, itemsArray);
            listView.setAdapter(adapter);
            buttonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int k = listView.getCheckedItemCount();
                    if (k == 0)
                        Toast.makeText(getApplicationContext(), "No item is chosen", Toast.LENGTH_SHORT).show();
                    else {
                        chosenItems = new ArrayList<String>();
                        for (int i = 0; i < listView.getCount(); i++)
                            if (listView.isItemChecked(i))
                                chosenItems.add(itemsArray[i]);
                        Intent intent = new Intent(getApplicationContext(), Shopping2Activity.class);
                        intent.putStringArrayListExtra("chosenItems", chosenItems);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        } else
            setContentView(R.layout.no_items);
    }
}