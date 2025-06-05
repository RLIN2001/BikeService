package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class NuovaPassword extends AppCompatActivity {
EditText pass1,pass2,pass3;
Button button;
String username;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_password);
        getSupportActionBar().hide();
        pass1=findViewById(R.id.passwordA);
        pass2=findViewById(R.id.passwordN);
        pass3=findViewById(R.id.passwordC);
        button=findViewById(R.id.button4);

        username=getIntent().getStringExtra("name");
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AggiornaPassword aggiorna=new AggiornaPassword();
                aggiorna.execute();
            }
        });



    }

    public void torna(View view) {
        finish();

    }



    private class AggiornaPassword extends AsyncTask<String,String,String> {

        String z="";
        String p=pass1.getText().toString();
        String p2=pass2.getText().toString();
        String p3=pass3.getText().toString();
        boolean isSuccess=false;
        String passwd;
        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Caricamento...");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(p.trim().equals("")|| p2.trim().equals("") || p3.trim().equals(""))
                z = "E' obbligatorio inserire le password....";
            else if(p.equals(p2))
                z="La nuova password non deve coincidere con quella vecchia";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Controlla la tua connessione internet";
                    } else {

                        String query=" select * from utente where username='"+username+"'";


                        Statement stmt = con.createStatement();

                        ResultSet rs=stmt.executeQuery(query);

                        if(rs.next())

                        {

                            passwd=rs.getString("password");

                            if(p.equals(passwd))
                            {
                                if(p3.equals(p2)) {
                                    query="update utente set password='"+p3+"' where username='"+username+"' ";
                                    PreparedStatement pst=con.prepareStatement(query);
                                    pst.executeUpdate();

                                    z="La password è stata aggiornata con successo";
                                    isSuccess = true;

                                }
                                else
                                    z="La password di conferma non coincide con la nuova password";

                            }

                            else {
                                z="La password attuale che è stata inserita non è corretta";

                            }
                        }
                        else {
                            z = "username non esistente, riprova oppure registrati";
                        }




                    }
                }
                catch (Exception ex)
                {
                    isSuccess=false;
                    z = "Exceptions"+ex;
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            if(isSuccess) {
                Toast.makeText(NuovaPassword.this,""+z,Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(NuovaPassword.this,login.class);
                startActivity(intent);
            }

            Toast.makeText(NuovaPassword.this,""+z,Toast.LENGTH_SHORT).show();

            progressDialog.hide();

        }
    }













}
