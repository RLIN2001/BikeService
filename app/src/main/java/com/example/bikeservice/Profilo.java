package com.example.bikeservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class Profilo extends AppCompatActivity {
    BottomNavigationView b;
    Button button,button2;
   TextView usern,usern2,email,tel;
   ImageButton imgbtm;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    String em;
    String Ntel;
    ArrayList<Integer> id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);
        getSupportActionBar().hide();

        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);

        final String username=getIntent().getStringExtra("name");
        em=getIntent().getStringExtra("email");
         Ntel=getIntent().getStringExtra("tel");
        final ArrayList<String> latitudine=getIntent().getExtras().getStringArrayList("latitudine");
        final ArrayList<String>longitudine=getIntent().getExtras().getStringArrayList("longitudine");

        id=getIntent().getExtras().getIntegerArrayList("id_bici");
        connectionClass = new ConnectionClass();
        usern=findViewById(R.id.username);
        usern2=findViewById(R.id.username2);
        email=findViewById(R.id.email);
        tel=findViewById(R.id.tel);
        button=findViewById(R.id.button3);
        button2=findViewById(R.id.button6);
        imgbtm=findViewById(R.id.imageButton2);



        b=findViewById(R.id.bottomNavigationView);
        b.setSelectedItemId(R.id.profilo);
        b.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.mappa:
                        Intent intent=new Intent(Profilo.this,Main2Activity.class);
                        intent.putExtra("name",username);
                        intent.putExtra("email",em);
                        intent.putExtra("tel",Ntel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);

                        return true;

                    case R.id.profilo:
                        return true;


                    case R.id.info:
                        intent=new Intent(Profilo.this,Info.class);
                        intent.putExtra("name",username);
                        intent.putExtra("email",em);
                        intent.putExtra("tel",Ntel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                }
                return false;
            }
        });

        usern.setText(username);
        usern2.setText(username);
        email.setText(em);
        tel.setText(Ntel);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profilo.this,NuovaPassword.class);
                intent.putExtra("name",username);
                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profilo.this,CambioDati.class);
                intent.putExtra("name",username);
                intent.putExtra("email",em);
                intent.putExtra("tel",Ntel);

                startActivity(intent);
                overridePendingTransition(0,0);

            }
        });



        imgbtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrdineUtente o=new OrdineUtente();
                o.execute();
            }
        });



    }



    public void exit(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Logout");
        builder.setMessage("Sicuro di voler uscire dall'account?");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Profilo.this,login.class);
                startActivity(intent);

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void inizia() {
       OrdineUtente o=new OrdineUtente();
       o.execute();

    }


    private class OrdineUtente extends AsyncTask<String,String,String> {

        String z = "";
        String username=usern.getText().toString();
        boolean v;
        ArrayList<String>id_bici=new ArrayList<>();
        ArrayList<String>latit=new ArrayList<>();
        ArrayList<String>longit=new ArrayList<>();
        ArrayList<String> orario=new ArrayList<>();
        ArrayList<String> orarioinizio=new ArrayList<>();
        ArrayList<String> orariofine=new ArrayList<>();




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

                   String  query = "delete from prenotazione_viaggio where utente_us='" + username + "' and " +
                            "inizio< CURRENT_TIME;";

                    Statement stmt = con.createStatement();

                    stmt.executeUpdate(query);








                    ResultSet rs;




                    query = "select bicicletta.id,bicicletta.latitudine,bicicletta.longitudine,prenotazione_viaggio.inizio,prenotazione_viaggio.fine " +
                            "from bicicletta,prenotazione_viaggio where prenotazione_viaggio.bicicletta_id=b" +
                            "icicletta.id and prenotazione_viaggio.utente_us='"+username+"'";

                         rs=stmt.executeQuery(query);


                        while (rs.next()) {
                            id_bici.add(Integer.toString(rs.getInt("id")));
                            latit.add(Double.toString(rs.getDouble("latitudine")));
                            longit.add(Double.toString(rs.getDouble("longitudine")));
                            orario.add(rs.getString("inizio")+"-"+rs.getString("fine"));
                            orarioinizio.add(rs.getString("inizio"));
                            orariofine.add(rs.getString("fine"));
                        }






                }
            } catch (Exception ex) {
                z = "Exceptions" + ex;
            }

            return z;

        }
        @Override
        protected void onPostExecute(String s) {
            if(!id_bici.isEmpty()) {

                Intent intent=new Intent(Profilo.this,Carrello.class);
                intent.putExtra("id_bici",id_bici);
                intent.putExtra("latitudine",latit);
                intent.putExtra("longitudine",longit);
                intent.putExtra("username",username);
                intent.putExtra("orari",orario);
                intent.putExtra("inizio",orarioinizio);
                intent.putExtra("fine",orariofine);

                intent.putExtra("email",em);
                intent.putExtra("tel",Ntel);

                startActivity(intent);
                overridePendingTransition(0,0);


            }
            else {
                Toast.makeText(Profilo.this, "Non hai ancora effettuato alcuna prenotazione" +
                        " oppure le prenotazioni eseguite precedentemente sono scadute", Toast.LENGTH_SHORT).show();


            }
            progressDialog.hide();

        }
    }










}