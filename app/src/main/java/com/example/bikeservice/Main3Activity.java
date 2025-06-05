package com.example.bikeservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.LocaleList;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class Main3Activity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;
    ProgressDialog progressDialog;
    ConnectionClass connectionClass;
    GoogleMap map;
    private GpsTracker gpsTracker;
    private TextView mTextViewCountDown;
    long mTimeLeftInMillis;
    private FusedLocationProviderClient client;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    Button button;
    String split;
    LocationManager locationManager;
    String id, username, email, tel;
    double latitudine;
    double longitudine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);
        getSupportActionBar().hide();
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        button = findViewById(R.id.button);
        mTimeLeftInMillis = getIntent().getLongExtra("timer", 0);
        connectionClass = new ConnectionClass();
        progressDialog = new ProgressDialog(this);
        id = getIntent().getStringExtra("id");
        email = getIntent().getStringExtra("email");
        tel = getIntent().getStringExtra("tel");
        username = getIntent().getStringExtra("username");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Main3Activity.this);

                builder.setTitle("Termine Viaggio");
                builder.setMessage("Sicuro di voler terminare il viaggio ora");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {



                        gpsTracker = new GpsTracker(Main3Activity.this);
                        if(gpsTracker.canGetLocation()){
                             latitudine = gpsTracker.getLatitude();
                             longitudine = gpsTracker.getLongitude();
                            aggiornamentodati();
                        }else{
                            gpsTracker.showSettingsAlert();
                        }


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



        startTimer();
        updateCountDownText();












        if(ActivityCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager
                .PERMISSION_GRANTED)

            posizioneAttuale();
        else
        {
            ActivityCompat.requestPermissions(Main3Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);


        }













    }

    private void aggiornamentodati() {
Inserimento e=new Inserimento();
e.execute();

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
                } else {
                    isSuccess=true;
                     Statement stmt = con.createStatement();


                   String query = "select prenotazione_viaggio.bicicletta_id from prenotazione_viaggio,bicicletta where prenotazione_" +
                            "viaggio.bicicletta_id=bicicletta.id";

                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        BiciPrenotata.add(rs.getInt("bicicletta_id"));
                    }

                    String regex = "\\[|\\]";
                    split = Arrays.toString(BiciPrenotata.toArray()).replaceAll(regex, "");




                    query = "delete from prenotazione_viaggio where utente_us='" + username + "' and " +
                            "bicicletta_id='" + id + "' ";

                    stmt.executeUpdate(query);

                    query = "update bicicletta set codice_attivazione='" + "" + "', latitudine="+latitudine+", longitudine="+longitudine+"where " +
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
                Intent intent = new Intent(Main3Activity.this, FineViaggio.class);
                intent.putExtra("email", email);
                intent.putExtra("tel", tel);
                intent.putExtra("name", username);
                intent.putExtra("s", split);

                startActivity(intent);
                overridePendingTransition(0, 0);
            }
            else
                Toast.makeText(Main3Activity.this, z, Toast.LENGTH_SHORT).show();
                progressDialog.hide();


        }
    }
























    private void startTimer() {


        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mTextViewCountDown.setText("0:00");

                aggiornamentodati();


                }




        }.start();
        mTimerRunning = true;






    }

    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);




    }

    private void posizioneAttuale() {
        Task<Location> task=client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if(location!=null)
                {
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {

                            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                            MarkerOptions options=new MarkerOptions().position(latLng).title("Posizione attuale");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

                            googleMap.addMarker(options);
                        }
                    });

                }
            }
        });

    }


    public void onRequestPermissionsResult(int requestCode,String []permissions,int []grantResults)
    {
        if(requestCode==44)
        {
            if(grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
            {

                posizioneAttuale();
            }


        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
    }
}
