package com.djwapps.daniel.rcremotecontrol;

import android.content.DialogInterface;
import android.icu.util.Output;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton startButton;
    private Button settingsButton,leftButton,rightButton,upButton,downButton;
    private boolean connectionEstablished;
    private String ip;


    public enum COMMAND {
        LEFT,RIGHT,UP,DOWN
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton: startConnection();
                break;
            case R.id.settingsButton: openSettingsMenu();
                break;
            case R.id.upButton: streamCommand(COMMAND.UP);
                break;
            case R.id.downButton:streamCommand(COMMAND.DOWN);
                break;
            case R.id.leftButton:streamCommand(COMMAND.LEFT);
                break;
            case R.id.rightButton:streamCommand(COMMAND.RIGHT);
                break;
        }
    }

    public void startConnection(){
        connectionEstablished = true;
    }



    private void openSettingsMenu(){
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("IP Settings");
        helpBuilder.setMessage("Set the LAN Address of RC");
        final EditText input = new EditText(this);
        input.setSingleLine();
        input.setText("");
        helpBuilder.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        ip = input.getText().toString();

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

    //This will open a socket, send the command, then close the socket
    public void streamCommand(final COMMAND command){
        if(connectionEstablished){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //Replace below IP with the IP of that device in which server socket open.
                        //If you change port then change the port number in the server side code also.
                        Socket s = new Socket(ip, 6666);

                        OutputStream out = s.getOutputStream();

                        PrintWriter output = new PrintWriter(out);

                        output.println(command.name().charAt(0));
                        output.flush();
                        output.close();
                        out.close();
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
    }



}
