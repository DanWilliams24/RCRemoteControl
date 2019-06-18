package com.djwapps.daniel.rcremotecontrol;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class networkin {

    public final String ip;
    public final int port;
    private boolean conEst = false;
    private Socket sock;
    private DataOutputStream dataOut;

    //constructor: establishes connection
    public networkin(String ipI, int portI) {
        ip = ipI;
        port = portI;
        try {
            sock = new Socket(ip, portI);
            dataOut = new DataOutputStream(this.sock.getOutputStream());
            conEst = true;

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    //overloaded for default case
    public networkin(String ip) {
        this(ip, 6666);
    }

    //accessor method: returns true if connection is established
    public boolean connected() {
        return (conEst);
    }

    //sends string encoded with UTF-8
    public void send(String command){
        try {

            dataOut.writeUTF(command);
            dataOut.flush();

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    //closes connection
    public void close(){
        try {
            this.dataOut.close();
            this.sock.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}