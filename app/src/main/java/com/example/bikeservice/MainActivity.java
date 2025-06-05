package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Intent i=new Intent(this,login.class);
        startActivity(i);

    }

    public void registrazione(View view) {
        Intent i=new Intent(this,Register.class);
        startActivity(i);
    }


}
