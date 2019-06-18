package com.djwapps.daniel.rcremotecontrol;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class networkin {

    //public final int defaultPort = 6666;
    public final String ip;
    public final int port;
    private boolean conEst = false;
    private Socket sock;

    //constructor: establishes connection
    public networkin(String ipI, int portI) {
        ip = ipI;
        port = portI;

        try {
            sock = new Socket(ip, portI);
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


/*
    //This will open a socket, send the command, then close the socket
    public void streamCommand( HMMM ){
        if(connectionEstablished){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        //Replace below IP with the IP of that device in which server socket open.
                        //If you change port then change the port number in the server side code also.

                        OutputStream out = s.getOutputStream();

                        PrintWriter output = new PrintWriter(out);

                        output.println(command.name().charAt(0));
                        output.flush();
                        output.close();
                        out.close();
                        s.close();
                }
            });

            thread.start();
        }
    }

}
*/