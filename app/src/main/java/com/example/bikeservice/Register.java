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
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

public class Register extends AppCompatActivity {
EditText us,em,tel,pass,pass2;
ConnectionClass connectionClass;
    ProgressDialog progressDialog;
Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

    us=findViewById(R.id.username);
    pass=findViewById(R.id.password);
    pass2=findViewById(R.id.password2);
    em=findViewById(R.id.email);
    tel=findViewById(R.id.tel);
    button=findViewById(R.id.button);

        connectionClass = new ConnectionClass();

        progressDialog=new ProgressDialog(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 Registrazione register = new Registrazione();
                 register.execute("");
            }
        });

    }







    public void OpenLogin(View view) {
        Intent i=new Intent(this,login.class);
        startActivity(i);
    }





    public class Registrazione extends AsyncTask<String,String,String>
    {


        String username=us.getText().toString();
        String password=pass.getText().toString();
        String password2=pass2.getText().toString();
        String email=em.getText().toString();
        String telefono=tel.getText().toString();
        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Caricamento...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if(username.trim().equals("")|| password.trim().equals("") ||password2.trim().equals("") || email.trim().equals("") || telefono.trim().equals("") )
                z = "Tutti i dati vanno compilati!";
            else
            {
                try {
                    Connection con = connectionClass.CONN();
                    if (con == null) {
                        z = "Controlla la tua connessione internet";
                    }
                    else if(!password2.equals(password))
                        z="La password di conferma non coincide con la password inserita";
                    else {

                        String query="insert into utente values('"+username+"','"+email+"','"+telefono+"','"+password+"')";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        z = "Registrazione avvenuta con successo";
                        isSuccess=true;


                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;

                    if (ex instanceof SQLIntegrityConstraintViolationException) {
                        if(ex.toString().contains("PRIMARY"))
                            z="L'username inserito e' gia' stato registrato come utente";
                        else
                            z="email inserito e' già stato registrato come utente!!";
                    } else {
                        // Other SQL Exception
                        z = "Exceptions"+ex;
                    }
                }
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();


            if(isSuccess) {
                startActivity(new Intent(Register.this,login.class));

            }

            progressDialog.hide();
        }
    }





}
