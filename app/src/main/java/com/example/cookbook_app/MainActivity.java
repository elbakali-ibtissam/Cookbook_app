package com.example.cookbook_app;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "myFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_page);
        Button log = findViewById(R.id.button2);
        Button sign = findViewById(R.id.button1);

        SharedPreferences sharedPreferences =getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String user =sharedPreferences.getString("username","Data not found");


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l1 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(l1);
                //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent l2 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(l2);
                // overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }
}