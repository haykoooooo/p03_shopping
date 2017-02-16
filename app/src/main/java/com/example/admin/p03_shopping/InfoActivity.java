package com.example.admin.p03_shopping;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        TextView textView = (TextView) findViewById(R.id.textInfo);
        textView.setText(getIntent().getStringExtra("info"));
    }
}