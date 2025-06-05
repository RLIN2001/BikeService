package com.example.bikeservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Prenotazione extends AppCompatActivity  {
TextView txt;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    String s;
 String username;
  String email;
  String tel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazione);

        txt=findViewById(R.id.text);

        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);
        getSupportActionBar().hide();
        username=getIntent().getStringExtra("name");
        email=getIntent().getStringExtra("email");
        s=getIntent().getStringExtra("s");
        tel=getIntent().getStringExtra("tel");



        txt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Torna t=new Torna();
        t.execute();
    }
});
    }












    public class Torna extends AsyncTask<String, String, String> {


        String z = "";

        boolean isSuccess=false;
        ArrayList<String>latitudine=new ArrayList<>();
        ArrayList<String>longitudine=new ArrayList<>();
        ArrayList<Integer>id_bici=new ArrayList<>();
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Caricamento...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Controlla la tua connessione internet";
                } else {

                    isSuccess=true;
                    String query = "select latitudine,longitudine,id from bicicletta where id not in(" + s + ")";
                    Statement stmt = con.createStatement();
                    ResultSet rs=stmt.executeQuery(query);


                    while (rs.next()) {
                       id_bici.add(rs.getInt("id"));
                        latitudine.add(Double.toString(rs.getDouble("latitudine")));
                        longitudine.add(Double.toString(rs.getDouble("longitudine")));

                    }
                }
            } catch (Exception ex) {

                isSuccess=false;
                z = "Exceptions" + ex;
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {

            if(isSuccess) {
                Intent intent = new Intent(Prenotazione.this, Main2Activity.class);
                intent.putExtra("name", username);
                intent.putExtra("email", email);
                intent.putExtra("tel", tel);
                intent.putExtra("latitudine", latitudine);
                intent.putExtra("longitudine", longitudine);
                intent.putExtra("id_bici", id_bici);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            else
                Toast.makeText(Prenotazione.this, z, Toast.LENGTH_SHORT).show();
            progressDialog.hide();

        }
    }









}