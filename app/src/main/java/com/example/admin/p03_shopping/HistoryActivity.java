package com.example.admin.p03_shopping;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    RadioButton[] r;
    ListView listView;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> shoppingArrayList;
    String[] shoppingArray;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor c;
    int cursorState = 1;
    String info = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingArrayList = new ArrayList<String>();
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
        c = db.rawQuery("select date from shopping order by date desc", null);
        ArrayList<String> shoppingList = new ArrayList<String>();
        if (c.moveToFirst())
            do {
                shoppingList.add(c.getString(c.getColumnIndex("date")));
            } while (c.moveToNext());
        c.close();
        if (shoppingList.size() == 0)
            setContentView(R.layout.no_shopping);
        else {
            shoppingArray = new String[shoppingList.size()];
            setContentView(R.layout.activity_history);
            r = new RadioButton[8];
            r[0] = (RadioButton) findViewById(R.id.radioButton);
            r[1] = (RadioButton) findViewById(R.id.radioButton2);
            r[2] = (RadioButton) findViewById(R.id.radioButton3);
            r[3] = (RadioButton) findViewById(R.id.radioButton4);
            r[4] = (RadioButton) findViewById(R.id.radioButton5);
            r[5] = (RadioButton) findViewById(R.id.radioButton6);
            r[6] = (RadioButton) findViewById(R.id.radioButton7);
            r[7] = (RadioButton) findViewById(R.id.radioButton8);
            listView = (ListView) findViewById(R.id.listView3);
            c = db.rawQuery("select date from shopping order by date desc", null);
            if (c.moveToFirst()) {
                do {
                    shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                } while (c.moveToNext());
            }
            c.close();
            for (int i = 0; i < shoppingArrayList.size(); i++)
                shoppingArray[i] = shoppingArrayList.get(i);
            arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
            listView.setAdapter(arrayAdapter);
            for (int i = 0; i < 8; i++)
                r[i].setOnClickListener(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    view.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.clickanim));
                    c = db.rawQuery("select details from shopping where date = '" + listView.getItemAtPosition(i) + "'", null);
                    if (c.moveToFirst()) {
                        do {
                            info = c.getString(c.getColumnIndex("details"));
                        } while (c.moveToNext());
                    }
                    c.close();
                    Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                    intent.putExtra("info", info);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.radioButton:
                r[0].setChecked(true);
                r[1].setChecked(false);
                r[2].setChecked(false);
                r[3].setChecked(false);
                r[4].setChecked(false);
                r[5].setChecked(false);
                if (r[6].isChecked())
                    cursorState = 1;
                else
                    cursorState = 7;
                for (int i = 3; i <= 5; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton2:
                r[0].setChecked(false);
                r[1].setChecked(true);
                r[2].setChecked(false);
                r[3].setChecked(false);
                r[4].setChecked(false);
                r[5].setChecked(false);
                if (r[6].isChecked())
                    cursorState = 2;
                else
                    cursorState = 8;
                for (int i = 3; i <= 5; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton3:
                r[0].setChecked(false);
                r[1].setChecked(false);
                r[2].setChecked(true);
                r[3].setChecked(false);
                r[4].setChecked(false);
                r[5].setChecked(false);
                if (r[6].isChecked())
                    cursorState = 3;
                else
                    cursorState = 9;
                for (int i = 3; i <= 5; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton4:
                r[0].setChecked(false);
                r[1].setChecked(false);
                r[2].setChecked(false);
                r[3].setChecked(true);
                r[4].setChecked(false);
                r[5].setChecked(false);
                if (r[6].isChecked())
                    cursorState = 4;
                else
                    cursorState = 10;
                for (int i = 0; i <= 2; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton5:
                r[0].setChecked(false);
                r[1].setChecked(false);
                r[2].setChecked(false);
                r[3].setChecked(false);
                r[4].setChecked(true);
                r[5].setChecked(false);
                if (r[6].isChecked())
                    cursorState = 5;
                else
                    cursorState = 11;
                for (int i = 0; i <= 2; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton6:
                r[0].setChecked(false);
                r[1].setChecked(false);
                r[2].setChecked(false);
                r[3].setChecked(false);
                r[4].setChecked(false);
                r[5].setChecked(true);
                if (r[6].isChecked())
                    cursorState = 6;
                else
                    cursorState = 12;
                for (int i = 0; i <= 2; i++)
                    r[i].setChecked(false);
                break;
            case R.id.radioButton7:
                r[6].setChecked(true);
                r[7].setChecked(false);
                if (cursorState > 6)
                    cursorState = cursorState - 6;
                break;
            case R.id.radioButton8:
                r[6].setChecked(false);
                r[7].setChecked(true);
                if (cursorState <= 6)
                    cursorState = cursorState + 6;
                break;
        }
        switch (cursorState) {
            case 1:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by date desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 2:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by items desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 3:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by expected desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 4:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by sum desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 5:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by (expected-sum) desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 6:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by 1-sum*1.0/expected desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 7:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by date desc", null);
                if (c.moveToLast()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToPrevious());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 8:
                shoppingArrayList.clear();
                c = db.query("shopping", null, null, null, null, null, null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 9:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by expected", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 10:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by sum", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 11:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by (sum-expected) desc", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
            case 12:
                shoppingArrayList.clear();
                c = db.rawQuery("select date from shopping order by 1-sum*1.0/expected", null);
                if (c.moveToFirst()) {
                    do {
                        shoppingArrayList.add(c.getString(c.getColumnIndex("date")));
                    } while (c.moveToNext());
                }
                c.close();
                for (int i = 0; i < shoppingArrayList.size(); i++)
                    shoppingArray[i] = shoppingArrayList.get(i);
                arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.oval, shoppingArray);
                listView.setAdapter(arrayAdapter);
                break;
        }
    }
}