package com.example.bikeservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Main2Activity extends FragmentActivity implements OnMapReadyCallback {
GoogleMap map;
ArrayList<LatLng>posizione;
    BottomNavigationView b;
    SupportMapFragment mapFragment;
ArrayList<Integer>id;
    String username,email,tel;

    ArrayList<String>latitudine;
    ArrayList<String>longitudine;


    private FusedLocationProviderClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mapFragment=(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



       username=getIntent().getStringExtra("name");
       email=getIntent().getStringExtra("email");
       tel=getIntent().getStringExtra("tel");

        id=getIntent().getExtras().getIntegerArrayList("id_bici");

       latitudine=getIntent().getExtras().getStringArrayList("latitudine");
       longitudine=getIntent().getExtras().getStringArrayList("longitudine");






        b=findViewById(R.id.bottomNavigationView);
        b.setSelectedItemId(R.id.mappa);
        b.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.profilo:


                        Intent intent=new Intent(Main2Activity.this,Profilo.class);

                        intent.putExtra("name",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);

                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.mappa:
                        return true;


                    case R.id.info:
                        intent=new Intent(Main2Activity.this,Info.class);
                        intent.putExtra("name",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);
                        intent.putExtra("latitudine",latitudine);
                        intent.putExtra("longitudine",longitudine);
                        intent.putExtra("id_bici",id);

                        startActivity(intent);
                        overridePendingTransition(0,0);


                }
                return false;
            }
        });


        client= LocationServices.getFusedLocationProviderClient(this);
        posizione=new ArrayList<>();


        for(int i=0;i<latitudine.size();i++)
        {
            posizione.add(new LatLng(Double.parseDouble(latitudine.get(i)),Double.parseDouble(longitudine.get(i))));
        }



        if(ActivityCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager
        .PERMISSION_GRANTED)

            posizioneAttuale();
            else
        {
           ActivityCompat.requestPermissions(Main2Activity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);


        }


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

    for(int i=0;i<posizione.size();i++)
        {

            map.addMarker(new MarkerOptions().position(posizione.get(i)).title(""+id.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.bicycle)));
            map.moveCamera(CameraUpdateFactory.newLatLng(posizione.get(i)));




        }



        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                LatLng p = arg0.getPosition();
                final String name = arg0.getTitle();

                if (!name.equals("Posizione attuale")) {

                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                            Main2Activity.this, R.style.BottomSheetDialogTheme);

                    final View bottomsheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet, (LinearLayout)
                            findViewById(R.id.bottomSheetContainer));


                    TextView t=bottomsheetview.findViewById(R.id.id);
                    t.setText("ID: "+name);

                    bottomsheetview.findViewById(R.id.prenota).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            bottomSheetDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);

                            builder.setTitle("Conferma");
                            builder.setMessage("Vuoi prenotare questa bicicletta?");

                            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(Main2Activity.this,Orario.class);
                                    intent.putExtra("id",name);
                                    intent.putExtra("name",username);
                                    intent.putExtra("email",email);
                                    intent.putExtra("tel",tel);
                                    startActivity(intent);
                                    overridePendingTransition(0,0);




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
                    bottomSheetDialog.setContentView(bottomsheetview);
                    bottomSheetDialog.show();

                }
                    return false;

            }
        });




    }



}
