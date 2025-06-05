package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Codice extends AppCompatActivity {
TextView tx;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;

    String id,username,email,tel;
    long TempoRimanentemls;

    Button button;
    ArrayList<String>latitudine;
    ArrayList<String>longitudine;
    String codice;
    String inizio;
    String fine;
    ArrayList<String>id_bici;
    ArrayList<String>orari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_codice);

        getSupportActionBar().hide();
        tx=findViewById(R.id.textview);
        button=findViewById(R.id.button5);

        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);

        codice=getIntent().getStringExtra("codice");
        username=getIntent().getStringExtra("username");
        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");

        inizio=getIntent().getStringExtra("inizio");
        fine=getIntent().getStringExtra("fine");

        id=getIntent().getStringExtra("id");

        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");
        latitudine=getIntent().getExtras().getStringArrayList("latitudine");
        longitudine=getIntent().getExtras().getStringArrayList("longitudine");
        id_bici=getIntent().getExtras().getStringArrayList("id_bici");
        orari=getIntent().getExtras().getStringArrayList("orari");

        tx.setText(codice);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Aggiornamento c=new Aggiornamento();
                c.execute();
            }
        });
    }







    private class Aggiornamento extends AsyncTask<String,String,String> {
        String codice = tx.getText().toString();
        String z = "";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Caricamento...");
            progressDialog.show();


            super.onPreExecute();
        }



        @Override
        protected String doInBackground(String... params) {


                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Controlla la tua connessione internet";
                    } else {
                       isSuccess=true;
                        String query = "";


                        query = "update bicicletta set codice_attivazione='" + codice + "' where " +
                                "id='" + id + "' ";
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.executeUpdate();




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
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                String time = format.format(calendar.getTime());


                String pattern = "HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                try {

                    Date date1 = sdf.parse(inizio);
                    Date date2 = sdf.parse(time);

                    TempoRimanentemls = date1.getTime() - date2.getTime();


                } catch (ParseException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(Codice.this, Time.class);

                intent.putExtra("username", username);
                intent.putExtra("email", email);
                intent.putExtra("tel", tel);
                intent.putExtra("id", id);
                intent.putExtra("inizio", inizio);
                intent.putExtra("fine", fine);
                intent.putExtra("tempor", TempoRimanentemls);

                intent.putExtra("orari", orari);
                intent.putExtra("id_bici", id_bici);
                intent.putExtra("latitudine", latitudine);
                intent.putExtra("longitudine", longitudine);
                startActivity(intent);

            }
            else
            Toast.makeText(Codice.this, "" + z, Toast.LENGTH_SHORT).show();
            progressDialog.hide();

        }
    }

}