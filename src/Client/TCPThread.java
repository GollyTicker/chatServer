package client;

import java.io.*;
import java.net.Socket;
import static utils.ClientProtocol.*;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class TCPThread extends Thread {
    Socket socket;
    BufferedReader reader;
    PrintWriter printer;
    String tcpHostname;
    int tcpPort;

    public TCPThread(String tcpHostname, int tcpPort) {
        this.tcpHostname = tcpHostname;
        this.tcpPort = tcpPort;
    }

    @Override
    public void run() {
        if(connectionToServerSuceeded()){
            printer.println(new_("Morozov"));
        }
    }

    private boolean connectionToServerSuceeded() {
        try {
            this.socket = new Socket(tcpHostname, tcpPort);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            printer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
            return false;
        }
    }
}
