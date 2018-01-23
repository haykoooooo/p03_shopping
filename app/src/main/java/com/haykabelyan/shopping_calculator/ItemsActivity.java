package com.haykabelyan.shopping_calculator;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemsActivity extends Activity {

    private ListView listView;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> itemsList;
    private Cursor c;
    private String[] itemsArray;
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            setContentView(R.layout.activity_items);
            itemsArray = new String[itemsList.size()];
            for (int i = 0; i < itemsList.size(); i++) {
                itemsArray[i] = itemsList.get(i);
            }
            adapter = new ArrayAdapter<String>(this, R.layout.oval, itemsArray);
            listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                int k;

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
                    listView.setEnabled(false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listView.setEnabled(true);
                        }
                    }, 300);
                    String title = itemsArray[i];
                    ad = new AlertDialog.Builder(ItemsActivity.this);
                    ad.setTitle(title);
                    ad.setMessage("Choose an action");
                    k = i;
                    ad.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                            intent.putExtra("oldName", itemsArray[k]);
                            startActivity(intent);
                        }
                    });
                    ad.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            final Context context = ItemsActivity.this;
                            String title = "Delete";
                            String message = "Do you want to delete the " + itemsArray[k] + "?";
                            AlertDialog.Builder ad = new AlertDialog.Builder(context);
                            ad.setTitle(title);
                            ad.setMessage(message);
                            ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {
                                    try {
                                        db.delete("items", "name = '" + itemsArray[k] + "'", null);
                                        startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                                        finish();
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "No such item.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int arg1) {

                                }
                            });
                            ad.setCancelable(true);
                            ad.show();
                        }
                    });
                    ad.setCancelable(true);
                    ad.show();
                }
            });
        } else
            setContentView(R.layout.no_items);
    }
}