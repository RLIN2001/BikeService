package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Orario extends AppCompatActivity {
    TimePicker picker;
    ImageButton btnGet;
    TextView tvw;

    String id,username;
    String email,tel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orario);
        getSupportActionBar().hide();


        username=getIntent().getStringExtra("name");

        id=getIntent().getStringExtra("id");


        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");


        tvw=(TextView)findViewById(R.id.textView1);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        btnGet=(ImageButton) findViewById(R.id.imageButton3);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23 ){
                    hour = picker.getHour();
                    minute = picker.getMinute();


                }
                else{
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }


                String s=String.format("%02d:%02d", hour, minute);



                Calendar calendar=Calendar.getInstance();
                SimpleDateFormat format=new SimpleDateFormat("HH:mm");
                String time=format.format(calendar.getTime());




                String pattern = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                try {

                    Date date1 = sdf.parse(s);
                    Date date2 = sdf.parse(time);


                    if (date1.before(date2))
                        Toast.makeText(Orario.this, "Ora di inizio non deve essere prima dell'ora attuale", Toast.LENGTH_SHORT).show();

                    else {
                        Intent intent = new Intent(Orario.this, Orario2.class);
                        intent.putExtra("inizioT", s);

                        intent.putExtra("id", id); //bicicletta selezionata
                        intent.putExtra("name", username);
                        intent.putExtra("email", email);
                        intent.putExtra("tel", tel);

                        startActivity(intent);
                        overridePendingTransition(0, 0);

                    }
                }catch (ParseException e) {
                    e.printStackTrace();
                }





            }
        });
    }
}

