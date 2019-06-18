package com.djwapps.daniel.rcremotecontrol;


import android.util.Log;

import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;

public class Networker {

    public final String ip;
    public final int port;
    private boolean connected = false;
    private Socket sock;
    private DataOutputStream dataOut;

    //constructor: establishes connection
    public Networker(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            sock = new Socket(ip, port);
            dataOut = new DataOutputStream(this.sock.getOutputStream());
            connected = true;

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //overloaded for default case
    public Networker(String ip) {
        this(ip, 6666);
    }

    //accessor method: returns true if connection is established
    public boolean isConnected() {
        return (connected);
    }

    //sends string encoded with UTF-8
    public void send(String command){
        Log.d("RC-NET", "Attempting to send Command: " + command);
        if(isConnected()){
            try {

                dataOut.writeUTF(command);
                dataOut.flush();
                Log.d("RC-NET", "Message Sent: " + command);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //Throw an error so that MainActivity can catch it and inform the user of the problem
            throw new IllegalStateException("Connection to server must be established before communications take place.");
        }
    }

    //closes connection
    public void close(){
        Log.d("RC-NET", "Closing Connection");
        try {
            this.dataOut.close();
            this.sock.close();
            Log.d("RC-NET", "Connection Closed");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}