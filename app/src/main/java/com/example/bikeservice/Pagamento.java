package com.example.bikeservice;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Pagamento extends AppCompatActivity {

    EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7;
    String id,username;
    String email,tel;
    Button btm;
    TextView visualizza;
    String serverMessage;
    String tempoM;
    String[] splited;
    String inizio;
    String fine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamento);
        getSupportActionBar().hide();
    ed1=findViewById(R.id.nome);
    ed2=findViewById(R.id.cognome);
    ed3=findViewById(R.id.carta);
    ed4=findViewById(R.id.data);
    ed5=findViewById(R.id.cvv);
    ed6=findViewById(R.id.codp);
    ed7=findViewById(R.id.ntel);
    btm=findViewById(R.id.button);
    visualizza=findViewById(R.id.visualizza);



    tempoM=getIntent().getStringExtra("tempoM");
    id=getIntent().getStringExtra("id");
        email=getIntent().getStringExtra("email");
        tel=getIntent().getStringExtra("tel");
        username=getIntent().getStringExtra("username");


        inizio=getIntent().getStringExtra("inizio");
        fine=getIntent().getStringExtra("fine");






    }

    public void startClient(View view) {
        if(ed1.getText().toString().trim().isEmpty() ||ed2.getText().toString().trim().isEmpty() ||ed3.getText().toString().trim().isEmpty() ||
                ed4.getText().toString().trim().isEmpty() ||ed5.getText().toString().trim().isEmpty() ||
                ed6.getText().toString().trim().isEmpty() ||ed7.getText().toString().trim().isEmpty())
        {
            Toast.makeText(Pagamento.this, "E' obbligatorio compilare tutti i dati richiesti", Toast.LENGTH_SHORT).show();
        }
        else {
            TcpClient tcpClient = new TcpClient("192.168.1.71", 8189);
            tcpClient.start();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            splited = serverMessage.split("\\s+");
            visualizza.setText("Importo: " + splited[0] + "€");
            alert();
        }

        }

    private void alert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Conferma");
        builder.setMessage("Confermare il pagamento?\n Importo da pagare: "+splited[0]+"€");

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Pagamento.this);

                builder.setTitle("Pagamento effettuato");
                builder.setIcon(R.drawable.ic_done_black_24dp);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {



                Intent intent=new Intent(Pagamento.this,Codice.class);

                intent.putExtra("codice",splited[1]);

                        intent.putExtra("username",username);
                        intent.putExtra("email",email);
                        intent.putExtra("tel",tel);
                        intent.putExtra("id",id);
                        intent.putExtra("inizio",inizio);
                        intent.putExtra("fine",fine);
                startActivity(intent);

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();




            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public class TcpClient extends Thread {
        private String SERVERIP;
        private int SERVERPORT;
        PrintWriter out;
        BufferedReader in;
        private boolean mRun = false;

        public TcpClient(String ipAddress, int port) {
            SERVERIP = ipAddress;
            SERVERPORT = port;
        }

        public void run() {

            mRun = true;
            Socket socket = null;
            try {
                socket = new Socket(SERVERIP, SERVERPORT);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));


                visualizza.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!(serverMessage == null)) {
                            //visualizza.setText(serverMessage);

                        }

                    }
                });


                out.println(tempoM);


                serverMessage = in.readLine();

                socket.close();

            } catch (Exception e) {
                System.out.println(e);

            }


        }

    }




}


















