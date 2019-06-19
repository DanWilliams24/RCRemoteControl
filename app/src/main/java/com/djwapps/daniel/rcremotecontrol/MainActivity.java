package com.djwapps.daniel.rcremotecontrol;

import android.content.DialogInterface;
import android.icu.util.Output;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton startButton;
    private Button settingsButton,leftButton,rightButton,upButton,downButton,stopButton;
    private TextView ipView;
    private String ip;
    private Boolean networkerEstablished = false;
    private Networker networker;

    public enum COMMAND {
        LEFT,RIGHT,UP,DOWN,STOP
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startButton = findViewById(R.id.startButton);
        settingsButton = findViewById(R.id.settingsButton);
        leftButton = findViewById(R.id.leftButton);
        rightButton = findViewById(R.id.rightButton);
        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
        ipView = findViewById(R.id.ipView);
        stopButton = findViewById(R.id.stopButton);
        ip = "255.255.255.255";
        startButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);

    }

    public void setIp(String ip) {
        this.ip = ip;
        ipView.setText(ip);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton:
                if(networkerEstablished && networker.isConnected()){
                    endConnection();
                }else{
                    startConnection();
                }
                break;
            case R.id.settingsButton:
                openSettingsMenu();
                break;
            case R.id.upButton:
                if(networkerEstablished){
                    Log.d("RC-UI", "UP Button Pressed");
                    attemptToSend("" + COMMAND.UP.name().charAt(0));
                }else{
                    Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.downButton:
                if(networkerEstablished){
                    Log.d("RC-UI", "DOWN Button Pressed");
                    attemptToSend("" + COMMAND.DOWN.name().charAt(0));
                }else{
                    Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.leftButton:
                if(networkerEstablished){
                    Log.d("RC-UI", "LEFT Button Pressed");
                    attemptToSend("" + COMMAND.LEFT.name().charAt(0));
                }else{
                    Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rightButton:
                if(networkerEstablished){
                    Log.d("RC-UI", "RIGHT Button Pressed");
                    attemptToSend("" + COMMAND.RIGHT.name().charAt(0));
                }else{
                    Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.stopButton:
                if(networkerEstablished){
                    Log.d("RC-UI", "STOP Button Pressed");
                    attemptToSend("" + COMMAND.STOP.name().charAt(0));
                }else{
                    Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT).show();
                }

        }

    }


    private void attemptToSend(String data){
        try {
            networker.send(data);
        }catch (IllegalStateException e){
            Toast.makeText(getApplicationContext(),"Error: " + e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void startConnection(){
        Log.d("RC-UI", "Attempting to Connect");
        settingsButton.setEnabled(false);
        networker = new Networker(ip);
        networkerEstablished = true;
    }


    public void endConnection(){
        Log.d("RC-UI", "Proceeding to End Connection");
        networker.close();
    }


    private void openSettingsMenu(){
        Log.d("RC-UI", "Opening Settings");
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(MainActivity.this);
        helpBuilder.setTitle("IP Settings");
        helpBuilder.setMessage("Set the LAN Address of RC");
        final EditText input = new EditText(this);
        input.setSingleLine();
        input.setText("");
        input.setHint(ip);
        helpBuilder.setView(input);

        helpBuilder.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        setIp(input.getText().toString());

                    }
                });

        helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });

        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();


    }

}
