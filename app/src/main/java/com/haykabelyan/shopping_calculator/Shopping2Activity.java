package com.haykabelyan.shopping_calculator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Shopping2Activity extends Activity {

    private TextView expected, cost;
    private Button buttonFinish;
    private GridView gridView, gridViewHeader;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<String> chosenItems;
    private ContentValues cv;
    private Cursor c;
    private String[] itemsArray;
    private ArrayAdapter<String> adapter, headerAdapter;
    private String price;
    private int selectedPosition;
    boolean doubleBackToExitPressedOnce = false;
    int exp = 0;
    int sum = 0;
    int savedSum = 0;
    String date, details, shoppingInfo;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Fragment fragment1 = getFragmentManager().findFragmentById(R.id.f1);
        Fragment fragment2 = getFragmentManager().findFragmentById(R.id.f2);

        buttonFinish = (Button) findViewById(R.id.buttonFinish);
        expected = (TextView) findViewById(R.id.expected);
        cost = (TextView) findViewById(R.id.cost);

        final Intent intent = getIntent();
        chosenItems = intent.getStringArrayListExtra("chosenItems");
        itemsArray = new String[3 * chosenItems.size() + 3];
        for (int i = 0, j = 0; i < 3 * chosenItems.size(); i += 3, j++) {
            itemsArray[i] = chosenItems.get(j);
            itemsArray[i + 1] = "0";
            itemsArray[i + 2] = "0";
        }
        itemsArray[3 * chosenItems.size()] = " Other (non planned) ";
        itemsArray[3 * chosenItems.size() + 1] = "0";
        itemsArray[3 * chosenItems.size() + 2] = "0";
        adapter = new ArrayAdapter<String>(this, R.layout.tableitem, R.id.tvTextt, itemsArray);
        gridView = (GridView) fragment2.getView().findViewById(R.id.gridView);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);

        headerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.headeritem, R.id.tvTextHeader, new String[]{"Item", "Expected", "Cost"});
        gridViewHeader = (GridView) fragment1.getView().findViewById(R.id.gridViewHeader);
        gridViewHeader.setAdapter(headerAdapter);
        gridViewHeader.setNumColumns(3);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.startAnimation(AnimationUtils.loadAnimation(Shopping2Activity.this, R.anim.clickanim));
                gridView.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setEnabled(true);
                    }
                }, 300);
                if (i % 3 == 1) {
                    selectedPosition = i;
                    Intent newIntent = new Intent(getApplicationContext(), PriceActivity.class);
                    startActivityForResult(newIntent, 1);
                }
                if (i % 3 == 2) {
                    selectedPosition = i;
                    if (itemsArray[i - 1].equals("0"))
                        Toast.makeText(getApplicationContext(), "First, enter the expected sum.", Toast.LENGTH_LONG).show();
                    else {
                        Intent newIntent = new Intent(getApplicationContext(), PriceActivity.class);
                        startActivityForResult(newIntent, 1);
                    }
                }
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = Shopping2Activity.this;
                String title = "Delete";
                String message = "Do you want to finish this shopping?";
                AlertDialog.Builder ad = new AlertDialog.Builder(context);
                ad.setTitle(title);
                ad.setMessage(message);
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        if (exp != 0) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            String hour = calendar.getTime().getHours() + "";
                            String minute = calendar.getTime().getMinutes() + "";
                            String day = calendar.get(calendar.DAY_OF_MONTH) + "";
                            String month = calendar.getTime().getMonth() + 1 + "";
                            String year = 1900 + calendar.getTime().getYear() + "";
                            if (Integer.parseInt(hour) < 10)
                                hour = "0" + hour;
                            if (Integer.parseInt(minute) < 10)
                                minute = "0" + minute;
                            if (Integer.parseInt(day) < 10)
                                day = "0" + day;
                            if (Integer.parseInt(month) < 10)
                                month = "0" + month;
                            date = year + "." + month + "." + day + ", " + hour + ":" + minute;
                            savedSum = exp - sum;

                            details = "";
                            for (int i = 0; i < itemsArray.length; i++) {
                                if (i % 3 == 0)
                                    details += itemsArray[i] + "-";
                                if (i % 3 == 1)
                                    details += "(" + itemsArray[i] + ")";
                                if (i % 3 == 2)
                                    details += itemsArray[i] + "\n";
                            }
                            details += "\n" + "Total - " + "(" + exp + "-expected) " + sum + " dram.\n";
                            if (exp >= sum)
                                details += "\n" + "Saved sum - " + savedSum + " dram.\n";
                            else
                                details += "\n" + "Wasted sum - " + (-savedSum) + " dram.\n";
                            if (exp >= sum)
                                details += "\n" + "Saved sum by percents - " + Math.floor((100 * (double) savedSum) / (double) exp * 100) / 100 + "%.\n";
                            else
                                details += "\n" + "Wasted sum by percents - " + (-Math.floor((100 * (double) savedSum) / (double) exp * 100) / 100) + "%.\n";
                            double savedPercent = (double) savedSum / (double) exp;
                            int iconNumber = 0;
                            if (savedSum < 0) {
                                savedSum = -savedSum;
                                savedPercent = (double) savedSum / (double) exp;
                                shoppingInfo = "You spent " + savedSum + " drams more than expected.";
                                iconNumber = 1;
                                if (savedPercent <= 0.05)
                                    shoppingInfo += "\n You paid almost as much as planned.";
                                if (savedPercent > 0.05 && savedPercent <= 0.1)
                                    shoppingInfo += "\n You paid a little more than planned.";
                                if (savedPercent > 0.1 && savedPercent <= 0.25)
                                    shoppingInfo += "\n You paid more than planned.";
                                if (savedPercent > 0.25 && savedPercent <= 0.5) {
                                    iconNumber = 2;
                                    shoppingInfo += "\n You paid much more than planned!";
                                }
                                if (savedPercent > 0.5) {
                                    iconNumber = 3;
                                    shoppingInfo += "\n What a disappointing shopping! Or maybe you didn't planned everything you bought...";
                                }
                                savedSum = -savedSum;
                                savedPercent = (double) savedSum / (double) exp;
                            }

                            if (savedSum > 0) {
                                savedPercent = (double) savedSum / (double) exp;
                                iconNumber = 4;
                                shoppingInfo = "You saved " + savedSum + " drams.";
                                if (savedPercent <= 0.05)
                                    shoppingInfo += "\n You paid almost as much as planned.";
                                if (savedPercent > 0.05 && savedPercent <= 0.1)
                                    shoppingInfo += "\n You paid a little less than planned.";
                                if (savedPercent > 0.1 && savedPercent <= 0.25)
                                    shoppingInfo += "\n You paid less than planned.";
                                if (savedPercent > 0.25 && savedPercent <= 0.5) {
                                    iconNumber = 5;
                                    shoppingInfo += "\n You paid much less than planned!";
                                }
                                if (savedPercent > 0.5) {
                                    iconNumber = 6;
                                    shoppingInfo += "\n What an excellent shopping! Or maybe You didn't buy everything you want...";
                                }
                            }

                            if (savedSum == 0) {
                                iconNumber = 7;
                                shoppingInfo = "No save, no waste.";
                            }

                            cv = new ContentValues();
                            cv.put("sum", sum);
                            cv.put("expected", exp);
                            cv.put("items", itemsArray.length / 3);
                            cv.put("details", details);
                            cv.put("date", date);
                            dbHelper = new DBHelper(getApplicationContext());
                            db = dbHelper.getWritableDatabase();
                            db.insert("shopping", null, cv);

                            String message = "The shopping is finished.\n" + shoppingInfo;
                            Drawable icon = null;
                            switch (iconNumber) {
                                case 1:
                                    icon = getResources().getDrawable(R.drawable.g1);
                                    break;
                                case 2:
                                    icon = getResources().getDrawable(R.drawable.g2);
                                    break;
                                case 3:
                                    icon = getResources().getDrawable(R.drawable.g3);
                                    break;
                                case 4:
                                    icon = getResources().getDrawable(R.drawable.b1);
                                    break;
                                case 5:
                                    icon = getResources().getDrawable(R.drawable.b2);
                                    break;
                                case 6:
                                    icon = getResources().getDrawable(R.drawable.b3);
                                    break;
                                case 7:
                                    icon = getResources().getDrawable(R.drawable.ok);
                                    break;
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(Shopping2Activity.this);
                            builder.setTitle("Finish").setMessage(message).setIcon(icon).setCancelable(false).setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            finish();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            AlertDialog.Builder builder0 = new AlertDialog.Builder(Shopping2Activity.this);
                            builder0.setTitle("Stop").setMessage("Unfinished shopping. Please exit or complete.")
                                    .setIcon(R.drawable.ok).setCancelable(false).setNegativeButton("ОК",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert0 = builder0.create();
                            alert0.show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        price = data.getStringExtra("price");
        exp = 0;
        sum = 0;
        itemsArray[selectedPosition] = price;
        for (int i = 0; i < itemsArray.length; i++) {
            if (i % 3 == 1)
                exp += Integer.parseInt(itemsArray[i]);
            if (i % 3 == 2)
                sum += Integer.parseInt(itemsArray[i]);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.tableitem, R.id.tvTextt, itemsArray);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
        expected.setText(exp + "");
        cost.setText(sum + "");
        gridView.post(new Runnable() {
            @Override
            public void run() {
                gridView.setSelection(selectedPosition);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < itemsArray.length; i++)
            outState.putString("i" + i, itemsArray[i]);
        outState.putString("exp", exp + "");
        outState.putString("sum", sum + "");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (int i = 0; i < itemsArray.length; i++)
            itemsArray[i] = savedInstanceState.getString("i" + i);
        exp = 0;
        sum = 0;
        for (int i = 0; i < itemsArray.length; i++) {
            if (i % 3 == 1)
                exp += Integer.parseInt(itemsArray[i]);
            if (i % 3 == 2)
                sum += Integer.parseInt(itemsArray[i]);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.tableitem, R.id.tvTextt, itemsArray);
        gridView.setAdapter(adapter);
        gridView.setNumColumns(3);
        expected.setText(exp + "");
        cost.setText(sum + "");
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "To exit, please click BACK again", Toast.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 5000);
    }
}