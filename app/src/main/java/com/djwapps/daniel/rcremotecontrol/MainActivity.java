package com.djwapps.daniel.rcremotecontrol;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    private ImageButton startButton;
    private Button settingsButton,leftButton,rightButton,upButton,downButton;
    private TextView ipView;
    private String ip;
    private Boolean networkerEstablished = false;
    private Networker networker;
    private Toast notificationBanner;
    private WorkerFragment mWorkerFragment,mRetainedFragment;


    public enum COMMAND {
        LEFT,RIGHT,UP,DOWN,STOP
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("ip", ip);
        outState.putBoolean("networkerEstablished", networkerEstablished);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        this.ip = savedInstanceState.getString("ip");
        this.networkerEstablished = savedInstanceState.getBoolean("networkerEstablished");
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
        ip = "255.255.255.255";
        startButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);

        leftButton.setOnTouchListener(this);
        rightButton.setOnTouchListener(this);
        upButton.setOnTouchListener(this);
        downButton.setOnTouchListener(this);


        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        upButton.setOnClickListener(this);
        downButton.setOnClickListener(this);


        notificationBanner = Toast.makeText(getApplicationContext(),"This device is not yet connected to the RC",Toast.LENGTH_SHORT);


        startButton.setColorFilter(getResources().getColor(R.color.offColor));

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        mWorkerFragment = (WorkerFragment) fm.findFragmentByTag("worker_fragment");

        // create the fragment and data the first time
        if (mWorkerFragment == null) {
            // add the fragment
            mWorkerFragment = new WorkerFragment();
            fm.beginTransaction().add(mWorkerFragment, "worker_fragment").commit();
            // load data from a data source or perform any calculation
            mWorkerFragment.setDataObject(networker);
        }

        networker = mWorkerFragment.getDataObject();



        // the data is available in mRetainedFragment.getData() even after
        // subsequent configuration change restarts.
    }

    public void setIp(String ip) {
        this.ip = ip;
        ipView.setText(ip);

    }



    private boolean onTouchDownCondition(View v, MotionEvent e, int id ){
        return (networkerEstablished && (v.getId() == id) && (e.getAction() == MotionEvent.ACTION_DOWN));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(onTouchDownCondition(v,event,R.id.leftButton)){
            Log.d("RC-UI", "LEFT Button Pressed");
            attemptToSend("" + COMMAND.LEFT.name().charAt(0));

        }else if(onTouchDownCondition(v,event,R.id.rightButton)){
            Log.d("RC-UI", "RIGHT Button Pressed");
            attemptToSend("" + COMMAND.RIGHT.name().charAt(0));

        }else if(onTouchDownCondition(v,event,R.id.upButton)){
            Log.d("RC-UI", "UP Button Pressed");
            attemptToSend("" + COMMAND.UP.name().charAt(0));

        }else if(onTouchDownCondition(v,event,R.id.downButton)){
            Log.d("RC-UI", "DOWN Button Pressed");
            attemptToSend("" + COMMAND.DOWN.name().charAt(0));
        }

        if((event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) && !networkerEstablished){
            notificationBanner.show();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startButton:
                if(networkerEstablished && networker.isConnected()){
                    endConnection();
                    setStartButtonColor();
                }else{
                    startConnection();
                    setStartButtonColor();

                }
                break;
            case R.id.settingsButton:
                openSettingsMenu();
                break;
        }

        if(v.getId() == R.id.leftButton || v.getId() == R.id.rightButton || v.getId() == R.id.upButton || v.getId() == R.id.downButton){
            attemptToSend("" + COMMAND.STOP.name().charAt(0));
        }

    }

    public void setStartButtonColor(){
        if(networkerEstablished){
            startButton.setColorFilter(getResources().getColor(R.color.onColor));
        }else{
            startButton.setColorFilter(getResources().getColor(R.color.offColor));
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
        Log.d("RC-NETUI", "Attempting to Connect");
        networker = new Networker(ip);
        android.os.SystemClock.sleep(5);
        if(networker.isConnected()){
            networkerEstablished = true;
            settingsButton.setEnabled(false);
        }else{
            networkerEstablished = false;
            Log.d("RC-NETUI", "Failed to Connect");
            Toast.makeText(getApplicationContext(),"Failed to Connect",Toast.LENGTH_SHORT).show();
        }

    }


    public void endConnection(){
        Log.d("RC-UI", "Proceeding to End Connection");
        networker.close();
        networkerEstablished = false;
        settingsButton.setEnabled(true);
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
