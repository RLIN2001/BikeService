package com.example.bikeservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class login extends AppCompatActivity {
Button button;
 ProgressDialog progressDialog;
 ConnectionClass connectionClass;
EditText us,pass;
    ArrayList<String> latitudine;
    ArrayList<String> longitudine;
    ArrayList<Integer> id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);
        us=findViewById(R.id.username);
        pass=findViewById(R.id.password);
        button=findViewById(R.id.button);

        latitudine=new ArrayList<>();
        longitudine=new ArrayList<>();
        id=new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dologin dologin=new Dologin();
                dologin.execute();
            }
        });




    }

    public void OpenRegister(View view) {
        Intent i = new Intent(this, Register.class);
        startActivity(i);
    }


    private class Dologin extends AsyncTask<String,String,String> {


        String username=us.getText().toString();
        String password=pass.getText().toString();


        String z="";
        boolean isSuccess=false;

        ArrayList<Integer> BiciPrenotata=new ArrayList<>();


        String u,p,email,tel;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Caricamento...");
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            if(username.trim().equals("")|| password.trim().equals(""))
                z = "E' obbligatorio inserire sia l'username che la password....";
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
                            u= rs.getString("username");
                            p=rs.getString("password");

                            email=rs.getString("email");
                            tel=rs.getString("tel");

                            if(password.equals(p)) {

                                isSuccess = true;
                                z = "Accesso effettuato";


                                query = "select prenotazione_viaggio.bicicletta_id from prenotazione_viaggio,bicicletta where prenotazione_" +
                                        "viaggio.bicicletta_id=bicicletta.id";

                                rs = stmt.executeQuery(query);

                                while (rs.next()) {
                                    BiciPrenotata.add(rs.getInt("bicicletta_id"));
                                }

                                String regex = "\\[|\\]";
                                String s = Arrays.toString(BiciPrenotata.toArray()).replaceAll(regex, "");

                                if (s.isEmpty()) {
                                    query = "select * from bicicletta";

                                    rs = stmt.executeQuery(query);

                                    while (rs.next()) {

                                        latitudine.add(Double.toString(rs.getDouble("latitudine")));
                                        longitudine.add(Double.toString(rs.getDouble("longitudine")));
                                        id.add(rs.getInt("id"));
                                    }
                                } else {
                                    query = "select latitudine,longitudine,id from bicicletta where id not in(" + s + ")";
                                    rs = stmt.executeQuery(query);


                                    while (rs.next()) {
                                        latitudine.add(Double.toString(rs.getDouble("latitudine")));
                                        longitudine.add(Double.toString(rs.getDouble("longitudine")));
                                        id.add(rs.getInt("id"));

                                    }


                                }
                            }

                                else {
                                    z="Password errata";
                                    isSuccess = false;

                                    }
                        }
                        else {
                            z = "username non esistente, riprova oppure registrati";
                        isSuccess=false;
                        }




                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions"+ex;
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {


            if(isSuccess) {

                Intent intent=new Intent(login.this,Main2Activity.class);
                intent.putExtra("name",username);
                intent.putExtra("email",email);
                intent.putExtra("tel",tel);


                    intent.putExtra("latitudine",latitudine);
                    intent.putExtra("longitudine",longitudine);





                      intent.putExtra("id_bici",id);

                startActivity(intent);

                progressDialog.hide();
                }
            Toast.makeText(login.this,""+z,Toast.LENGTH_SHORT).show();
            progressDialog.hide();


        }




        }



    }













