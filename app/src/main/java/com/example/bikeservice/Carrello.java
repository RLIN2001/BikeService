package com.example.bikeservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Carrello extends AppCompatActivity {

    ListView listView;
    int index=0;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
String username;
    ArrayList<String>latitudine;
    ArrayList<String>longitudine;
    ArrayList<String>id_bici;
    ArrayList<String>orari;
    ArrayList<String>inizio;
    ArrayList<String>fine;
    long sottrazione;
    String tempo;

    String split;
    String email;
    String tel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrello);
        getSupportActionBar().hide();
        listView = findViewById(R.id.listView);
        // now create an adapter class
        connectionClass = new ConnectionClass();
        progressDialog=new ProgressDialog(this);
        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");
        latitudine=getIntent().getExtras().getStringArrayList("latitudine");
        longitudine=getIntent().getExtras().getStringArrayList("longitudine");
        id_bici=getIntent().getExtras().getStringArrayList("id_bici");
        orari=getIntent().getExtras().getStringArrayList("orari");
        username=getIntent().getStringExtra("username");
        inizio=getIntent().getExtras().getStringArrayList("inizio");
        fine=getIntent().getExtras().getStringArrayList("fine");



        final CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), id_bici, latitudine,longitudine,orari);
        listView.setAdapter(customAdapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Carrello.this);
                builder.setIcon(R.drawable.remove);
                builder.setTitle("Elimina prenotazione");
                builder.setMessage("Vuoi eliminare la prenotazione di questa bici?");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                index=position;
                                eliminaPrenotazione();
                            }
                        });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();


                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Carrello.this);




                builder.setTitle("Inizio Viaggio");
                builder.setMessage("Sicuro di iniziare il viaggio ora?");
                builder.setIcon(R.drawable.bicycle2);
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {


                        String pattern = "HH:mm";
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

                        try {

                            Date date1 = sdf.parse(inizio.get(position));
                            Date date2 = sdf.parse(fine.get(position));


                            sottrazione = date2.getTime() - date1.getTime();
                            tempo= String.valueOf(TimeUnit.MILLISECONDS.toMinutes(sottrazione));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }




                        Intent intent=new Intent(Carrello.this,Pagamento.class);

                        intent.putExtra("username",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);

                        intent.putExtra("tempoM",tempo);


                        intent.putExtra("id",id_bici.get(position));
                        intent.putExtra("inizio",inizio.get(position));
                        intent.putExtra("fine",fine.get(position));



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
        });
    }



    private void eliminaPrenotazione() {
        Elimina e=new Elimina();
        e.execute();
    }



    public void back(View view) {
    finish();
    }


    private class Elimina extends AsyncTask<String,String,String> {

        String z = "";
        ArrayList<String>lat=new ArrayList<>();
        ArrayList<String>longit=new ArrayList<>();
        ArrayList<Integer>bici=new ArrayList<>();
        ArrayList<String>NewLat=new ArrayList<>();
        ArrayList<String>NewLong=new ArrayList<>();
        ArrayList<Integer>NewBici=new ArrayList<>();
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
                    Statement stmt = con.createStatement();
                    String query = "";








                   query = "delete from prenotazione_viaggio where utente_us='" + username + "' and " +
                            "bicicletta_id='" + id_bici.get(index) + "' ";

                    stmt.executeUpdate(query);








                    query = "select bicicletta.id,bicicletta.latitudine,bicicletta.longitudine,prenotazione_viaggio.inizio,prenotazione_viaggio.fine " +
                            "from bicicletta,prenotazione_viaggio where prenotazione_viaggio.bicicletta_id=b" +
                            "icicletta.id";

                    ResultSet rs=stmt.executeQuery(query);

                    while (rs.next()) {
                        bici.add(rs.getInt("id"));
                        lat.add(Double.toString(rs.getDouble("latitudine")));
                        longit.add(Double.toString(rs.getDouble("longitudine")));
                    }


                    String regex = "\\[|\\]";
                    split = Arrays.toString(bici.toArray()).replaceAll(regex, "");



                    query = "select bicicletta.latitudine,bicicletta.longitudine,id from bicicletta where id not in(" + split + ")";
                    rs = stmt.executeQuery(query);



                    while (rs.next()) {
                       NewBici.add(rs.getInt("id"));
                        NewLat.add(Double.toString(rs.getDouble("latitudine")));
                        NewLong.add(Double.toString(rs.getDouble("longitudine")));

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
                Toast.makeText(Carrello.this, "La bicicletta è stata rimossa dalla lista di prenotazione con successo"
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Carrello.this, Profilo.class);
                intent.putExtra("name", username);
                intent.putExtra("email", email);
                intent.putExtra("tel", tel);
                intent.putExtra("latitudine", NewLat);
                intent.putExtra("longitudine", NewLong);
                intent.putExtra("id_bici", NewBici);


                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            else
            Toast.makeText(Carrello.this, "" + z, Toast.LENGTH_SHORT).show();
            progressDialog.hide();
        }
    }






















    public class CustomAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflter;
        ArrayList<String> id_bici;ArrayList<String> latitudine; ArrayList<String> longitudine;ArrayList<String> orari;

        public CustomAdapter(Context applicationContext, ArrayList<String> id_bici, ArrayList<String> latitudine, ArrayList<String> longitudine,ArrayList<String> orari) {
            this.context = context;
            this.id_bici=id_bici;
            this.latitudine=latitudine;
            this.longitudine=longitudine;
            this.orari=orari;
            inflter = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return id_bici.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflter.inflate(R.layout.row, null);

            ImageView icon = (ImageView) view.findViewById(R.id.image);
            TextView idbike = (TextView)           view.findViewById(R.id.textView1);
            TextView posizione = (TextView)           view.findViewById(R.id.textView2);
            TextView orario=(TextView) view.findViewById(R.id.orario);
            posizione.setText("Latitudine:\n"+latitudine.get(i)+"\nLongitudine:"+longitudine.get(i));
            orario.setText("Orario: "+orari.get(i));
            icon.setImageResource(R.drawable.bicycle2);
            idbike.setText("ID BICICLETTA:"+id_bici.get(i));
            return view;
        }

        }






}