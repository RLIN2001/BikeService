package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class CambioDati extends AppCompatActivity {
EditText ed2,ed3;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    Button button;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambio_dati);
        getSupportActionBar().hide();
        username=getIntent().getStringExtra("name");
        final String email=getIntent().getStringExtra("email");
        final String tel=getIntent().getStringExtra("tel");

        ed2=findViewById(R.id.email);
        ed3=findViewById(R.id.tel);
        button=findViewById(R.id.button4);
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);

        ed2.setHint(email);
        ed3.setHint(tel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cambiodati c=new cambiodati();
                c.execute();
            }
        });





    }

    public void torna(View view) {

        finish();
    }


    private class cambiodati extends AsyncTask<String,String,String> {

        String z="";
        String p2=ed2.getText().toString();
        String p3=ed3.getText().toString();
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Caricamento...");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            if (p2.trim().equals("") && p3.trim().equals("")) {
                z = "I dati non sono stati modificati perché entranbi sono dati attuali. Se non desideri modificare l'email e il numero di telefono" +
                        " ,allora torna alla pagina precedente";
                isSuccess=false;

            }
        else {
                if (p2.trim().equals(""))
                    p2 = ed2.getHint().toString();

                if (p3.trim().equals(""))

                    p3 = ed3.getHint().toString();


                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Controlla la tua connessione internet";
                    } else {

                        String query = "";


                        query = "update utente set email='" + p2 + "' , tel='" + p3 + "' where " +
                                "username='" + username + "' ";
                        PreparedStatement pst = con.prepareStatement(query);
                        pst.executeUpdate();

                        z = "Le modifiche sono state effettuate con successo";
                        isSuccess = true;


                    }
                } catch (Exception ex) {
                    if (ex instanceof SQLIntegrityConstraintViolationException) {
                        z="email inserito e' già stato registrato come utente!!";
                    }
                    else {
                        z = "Exceptions" + ex;
                    isSuccess=false;
                    }
                }
            }
                return z;

        }
        @Override
        protected void onPostExecute(String s) {
            if(isSuccess) {
                Toast.makeText(CambioDati.this, "" + z, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(CambioDati.this,login.class);
                startActivity(intent);
            }
            else
            Toast.makeText(CambioDati.this, "" + z, Toast.LENGTH_SHORT).show();
            progressDialog.hide();

        }
    }


}
