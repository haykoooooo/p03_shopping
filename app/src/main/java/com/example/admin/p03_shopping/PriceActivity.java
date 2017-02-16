package com.example.admin.p03_shopping;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PriceActivity extends AppCompatActivity {

    private EditText text;
    private Button buttonOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Enter ","+");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price);
        text = (EditText) findViewById(R.id.editTextp);
        buttonOK = (Button) findViewById(R.id.oKButton);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Enter the sum.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent();
                    intent.putExtra("price", text.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}