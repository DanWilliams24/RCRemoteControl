package com.djwapps.daniel.rcremotecontrol;


import android.util.Log;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.lang.Thread;

public class Networker {

    public final String ip;
    public final int port;
    public String command;
    private String lastMsg = "";
    private boolean connected = false;
    private Socket sock;
    private DataOutputStream dataOut;
    private Thread worker1;


    private Thread sender = new Thread(new Runnable() {
        @Override
        public void run() {
            senderThread();
        }
    });

    //constructor: establishes connection
    public Networker(String ip, int port) {
        this.ip = ip;
        this.port = port;
        worker1 = new Thread(new Runnable() {
            @Override
            public void run() {
                createSocket();
            }
        });
        worker1.start();
    }

    //overloaded for default case
    public Networker(String ip) {
        this(ip, 6666);
    }

    private void createSocket() {
        try{
            sock = new Socket(ip, port);
            this.connected = true;
            dataOut = new DataOutputStream(this.sock.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
            connected = false;
        }
    }

    //accessor method: returns true if connection is established
    public boolean isConnected() {
        return (this.connected);
    }

    //sends string encoded with UTF-8
    public void send(String command){
        Log.d("RC-NET", "Attempting to send Command: " + command);
        this.command = command;

    }


    private void senderThread() {
        while (true) {
            try {
                if (this.command != this.lastMsg) {
                    this.lastMsg = this.command;
                    Log.d("RC-NET", "Message about to send" + this.command);
                    dataOut.writeUTF(this.command);
                    dataOut.flush();
                    Log.d("RC-NET", "Message Sent: " + this.command);

                }
                Thread.sleep(200);

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("RC-NET", "Message was not able to send: " + this.command);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("RC-NET", "Thread failed to stop");

            }
        }
    }


    //closes connection. A new instance of the Networker class will have to be created to start a new connection.
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