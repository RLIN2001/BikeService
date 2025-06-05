package com.example.bikeservice;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class Orario2 extends AppCompatActivity {
    TimePicker picker;
    Button btnGet, btmexit;
    TextView tvw;
    String fineT;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    String s;






    String username;
    String id;
    String email, tel;
    String split;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orario2);
        getSupportActionBar().hide();


        tvw = (TextView) findViewById(R.id.textView1);
        picker = (TimePicker) findViewById(R.id.timePicker1);
        btnGet = (Button) findViewById(R.id.button6);

        email = getIntent().getStringExtra("email");
        tel = getIntent().getStringExtra("tel");

        username = getIntent().getStringExtra("name");

        id = getIntent().getStringExtra("id");


        connectionClass = new ConnectionClass();

        progressDialog = new ProgressDialog(this);

        s = getIntent().getStringExtra("inizioT");
        picker.setIs24HourView(true);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour, minute;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = picker.getHour();
                    minute = picker.getMinute();


                } else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }


                fineT = String.format("%02d:%02d", hour, minute);



                String pattern = "HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                try {
                    Date date1 = sdf.parse(s);
                    Date date2 = sdf.parse(fineT);

                    if (date2.before(date1)) {
                        Toast.makeText(Orario2.this, "L'ora di fine deve essere dopo l'ora di inizio", Toast.LENGTH_SHORT).show();

                    } else {
                        // Toast.makeText(Orario2.this,"Good job",Toast.LENGTH_SHORT).show();


                        Inserimento u = new Inserimento();
                        u.execute();



                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });




                /*Calendar calendar= Calendar.getInstance();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm");
                String time=simpleDateFormat.format(calendar.getTime());

                tvw.setText(time);
                */


    }


    public class Inserimento extends AsyncTask<String, String, String> {


        String z = "";
        boolean isSuccess = false;
        ArrayList<Integer> BiciPrenotata = new ArrayList<>();

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
                    String query = "insert into prenotazione_viaggio values(NULL,'" + s + "','" + fineT + "','" + username + "','" + id + "')";
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);


                    query = "select prenotazione_viaggio.bicicletta_id from prenotazione_viaggio,bicicletta where prenotazione_" +
                            "viaggio.bicicletta_id=bicicletta.id";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        BiciPrenotata.add(rs.getInt("bicicletta_id"));
                    }

                    String regex = "\\[|\\]";
                    split = Arrays.toString(BiciPrenotata.toArray()).replaceAll(regex, "");



                    query = "update bicicletta set codice_attivazione='" + "" + "' where " +
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
                Intent intent = new Intent(Orario2.this, Prenotazione.class);
                intent.putExtra("email", email);
                intent.putExtra("tel", tel);
                intent.putExtra("name", username);
                intent.putExtra("s", split);
                startActivity(intent);
            }
            else
                Toast.makeText(Orario2.this, z, Toast.LENGTH_SHORT).show();
            progressDialog.hide();

        }
    }
}







