package client;

import client.GUI.GUIServices;
import utils.ServerProtocol;

import java.io.*;
import java.net.Socket;
import java.util.List;

import static utils.ClientProtocol.*;
import static utils.ServerProtocol.*;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class TCPThread extends Thread {
    Socket socket;
    BufferedReader reader;
    PrintWriter printer;
    String tcpHostname;
    int tcpPort;
    GUIServices guiServices;
    public boolean keepRunning = true;
    String registeredUsername;

    public TCPThread(String tcpHostname, int tcpPort, GUIServices guiServices) {
        this.tcpHostname = tcpHostname;
        this.tcpPort = tcpPort;
        this.guiServices = guiServices;
    }

    @Override
    public void run() {
        if (connectionToServerSuceeded()) {
            while (keepRunning) {
                registerName();

                // System.out.println(tokens);
                println(info());

            }
            closeAll();
        }
    }

    private void registerName() {
        while (registeredUsername == null) {
            String userName = guiServices.getUserName();    // blokierender Aufruf der auf den UserNamen wartet.
            println(new_(userName));
            System.out.println("Sent: " + userName);
            List<String> tokens;
            try {
                tokens = ServerProtocol.tokenize(reader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
                keepRunning = false;
                break;
            }
            System.out.println("Recv: " + tokens);
            if(isSucess(tokens)) {  // if the registration succeeded
                registeredUsername = userName;  // save the username. this will also end the loop.
                guiServices.nameRegistrationResponse(OK);   // tell this to the gui
            }
            else {
                guiServices.nameRegistrationResponse(tokens.get(1));    // tell the gui that it failed and start from beginning
            }
        }
    }

    private void closeAll() {
        try {
            reader.close();
            printer.close();
            socket.close();
        } catch (IOException e) {
        }
    }

    private void println(String s) {
        printer.println(s);
        printer.flush();
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
