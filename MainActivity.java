package com.example.viewslotadvance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void FindByCity(View view){

        Intent intent = new Intent(this, by_city.class);
        startActivity(intent);

    }

    public void FindByZip(View view){

        Intent intent = new Intent(this, by_zip.class);
        startActivity(intent);

    }
}