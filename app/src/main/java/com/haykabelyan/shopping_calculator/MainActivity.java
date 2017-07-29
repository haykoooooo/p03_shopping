package com.haykabelyan.shopping_calculator;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button b0, b1, b2, b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b0 = (Button) findViewById(R.id.button8);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button0);
        b0.setOnClickListener(this);
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "Help");
        menu.add(1, 2, 2, "About");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "     Version 1.0 \n By Hayk Abelyan \n 2016 " +
                        "September", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(final View view) {
        view.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.clickanim));
        view.setEnabled(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 300);
        switch (view.getId()) {
            case R.id.button:
                startActivity(new Intent(getApplicationContext(), ItemsActivity.class));
                break;
            case R.id.button2:
                startActivity(new Intent(getApplicationContext(), ShoppingActivity.class));
                break;
            case R.id.button0:
                startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                break;
            case R.id.button8:
                startActivity(new Intent(getApplicationContext(), AddActivity.class));
                break;
        }
    }
}