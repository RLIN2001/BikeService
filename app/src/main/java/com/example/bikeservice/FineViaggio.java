package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class FineViaggio extends AppCompatActivity {
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
        setContentView(R.layout.activity_fine_viaggio);

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


                tornaMappa t=new tornaMappa();
                t.execute();

                     }
        });
    }












    public class tornaMappa extends AsyncTask<String, String, String> {


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
                Intent intent = new Intent(FineViaggio.this, Main2Activity.class);
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
                Toast.makeText(FineViaggio.this,""+z,Toast.LENGTH_SHORT).show();
                progressDialog.hide();

        }
    }









}