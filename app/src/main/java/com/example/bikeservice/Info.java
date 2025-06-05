package com.example.bikeservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Info extends AppCompatActivity {
    BottomNavigationView b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().hide();

        final String username=getIntent().getStringExtra("name");
        final String email=getIntent().getStringExtra("email");
        final String tel=getIntent().getStringExtra("tel");
        final ArrayList<String> latitudine=getIntent().getExtras().getStringArrayList("latitudine");
        final ArrayList<String>longitudine=getIntent().getExtras().getStringArrayList("longitudine");
        final ArrayList<Integer> id=getIntent().getExtras().getIntegerArrayList("id_bici");


        b=findViewById(R.id.bottomNavigationView);
        b.setSelectedItemId(R.id.info);
        b.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.mappa:
                        Intent intent=new Intent(Info.this,Main2Activity.class);
                        intent.putExtra("name",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);

                        return true;

                    case R.id.profilo:
                         intent=new Intent(Info.this,Profilo.class);
                        intent.putExtra("name",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.info:
                        return true;

                }
                return false;
            }
        });

    }
}
