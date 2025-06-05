package com.example.bikeservice;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Time extends AppCompatActivity {
    private TextView mTextViewCountDown;
    private Button mButtonStartPause;


    String inizio;
    String fine;

    long timeR;
    String id,username,email,tel;
      private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        getSupportActionBar().hide();


        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        username=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");

        inizio=getIntent().getStringExtra("inizio");
        fine=getIntent().getStringExtra("fine");

        id=getIntent().getStringExtra("id");



        mTimeLeftInMillis=getIntent().getLongExtra("tempor",0);
        startTimer();






        updateCountDownText();
    }
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTextViewCountDown.setText("0:00");


                Toast.makeText(Time.this,"Inizio viaggio",Toast.LENGTH_SHORT).show();


                String pattern = "HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                try {

                    Date date1 = sdf.parse(inizio);
                    Date date2 = sdf.parse(fine);

                    long TempoRimanentemls=date2.getTime()-date1.getTime();

                    Intent intent=new Intent(Time.this,Main3Activity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("email",email);
                    intent.putExtra("tel",tel);
                    intent.putExtra("id",id);
                    intent.putExtra("timer",TempoRimanentemls);
                    startActivity(intent);



                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        }.start();
        mTimerRunning = true;
    }





    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);

    }

}