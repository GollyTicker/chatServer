package Client;

import Client.GUI.GUIServices;
import Client.GUI.StorageServices;
import utils.User;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static utils.ClientProtocol.*;
import static utils.ServerProtocol.*;
import static utils.ServerProtocol.bye;

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
    StorageServices storageServices;

    public TCPThread(String tcpHostname, int tcpPort, GUIServices guiServices, StorageServices storage) {
        this.tcpHostname = tcpHostname;
        this.tcpPort = tcpPort;
        this.guiServices = guiServices;
        this.storageServices = storage;
    }

    @Override
    public void run() {
        if (connectionToServerSuceeded()) {
            registerName();
            while (storageServices.isRunning()) {
                println(info());
                try {
                    List<String> tokens = readFromServer();
                    int numOfUsers = Integer.parseInt(tokens.get(1));
                    List<User> users = fromTokens(numOfUsers, tokens.subList(2,tokens.size()));
                    System.out.println("Users: " + users);
                    storageServices.saveUserList(users);
                } catch (Exception e) {
                    e.printStackTrace();
                    storageServices.stop();
                }
                try {
                    Thread.sleep(CLIENT_INFO_WAIT_MS);
                } catch (InterruptedException e) {
                    break;
                }
            }
            sendByeToServer();
            closeAll();
        }
    }

    private void sendByeToServer() {
        println(bye());
        try {
            reader.readLine();  // receive response and ignore it
        } catch (IOException e) {
        }
        storageServices.stop(); // sicherheitshalber nochmal stoppen
    }


    private List<String> readFromServer() throws IOException {
        return tokenize(reader.readLine());
    }

    private List<User> fromTokens(int numOfUsers, List<String> strings) throws UnknownHostException {
        List<User> ls = new ArrayList<User>();
        for(int i = 0; i < numOfUsers*2; i+=2) {
            User u = new User(strings.get(i), InetAddress.getByName(strings.get(i+1)));
            ls.add(u);
        }
        return ls;
    }

    private void registerName() {
        while (registeredUsername == null) {
            String userName = guiServices.getUserName();    // blokierender Aufruf der auf den UserNamen wartet.
            println(new_(userName));
            System.out.println("Sent: " + userName);
            List<String> tokens;
            try {
                tokens = readFromServer();
            } catch (IOException e) {
                System.err.println(e.getMessage());
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
            System.out.println("Connection failure: " + e.getMessage());
            return false;
        }
    }
}
