package com.example.admin.p03_shopping;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemsActivity extends Activity {

    private ListView listView;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> itemsList;
    private ContentValues cv;
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
                    String title = itemsArray[i];
                    String message = "Choose an action:";
                    String button1String = "Edit";
                    String button2String = "Delete";
                    ad = new AlertDialog.Builder(ItemsActivity.this);
                    ad.setTitle(title);
                    ad.setMessage(message);
                    k = i;
                    ad.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            Intent intent = new Intent(getApplicationContext(), UpdateActivity.class);
                            intent.putExtra("oldName", itemsArray[k]);
                            startActivity(intent);
                            finish();
                        }
                    });
                    ad.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
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
                    ad.setCancelable(true);
                    ad.show();
                }
            });
        } else
            setContentView(R.layout.no_items);
    }
}