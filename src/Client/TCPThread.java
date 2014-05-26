package Client;

import Client.GUI.GUIServices;
import Client.ThreadSafeData.StorageServices;
import utils.ClientProtocol;
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
        System.out.println("TCP Thread started.");

        // falls Connection fehlschlägt, dann brich ab.
        if (!connectionToServerSuceeded()) return;

        // ask for name (mutltiple times).
        // wird erfolgreich, oder bricht mit storageService.stop() ab.
        registerName();

        //
        while (storageServices.isRunning()) {
            try {
                println(info());    // send the server an info command
                List<String> tokens = readFromServer(); //  get response
                updateLocalUserList(tokens);
                guiServices.refreshUserList();
                Thread.sleep(CLIENT_INFO_WAIT_MS);  // wait a few seconds
            } catch (Exception e) { // if anything goes wrong. safe-kill the app.
                e.printStackTrace();
                storageServices.stop();
            }
        }
        // if the app is supposed to stop, then close the resources
        sendByeToServer();
        closeAll();
    }

    private void updateLocalUserList(List<String> tokens) throws UnknownHostException {
        int numOfUsers = Integer.parseInt(tokens.get(1));   // get the size of the userlist
        List<User> users = fromTokens(numOfUsers, tokens.subList(2, tokens.size())); // parse the userlist
        System.out.println("Users: " + users);
        storageServices.saveUserList(users);    // save the new list
    }

    private void sendByeToServer() {
        println(bye());
        try {
            reader.readLine();  // receive response and ignore it
            storageServices.guiMayQuit();
        } catch (IOException e) {
        }
        storageServices.stop(); // sicherheitshalber nochmal stoppen
    }

    // liest vom Server und gibt Tokens zurück anstatt den reinen String
    private List<String> readFromServer() throws IOException {
        return tokenize(reader.readLine());
    }

    // ["IvanMorozov", "127.0.0.1", "SwaneetSahoo", "123.222.222.111"]
    // => [User(IvanMorozov, 127.0.0.1), User(SwaneetSahoo, 123.222.222.111)]
    private List<User> fromTokens(int numOfUsers, List<String> strings) throws UnknownHostException {
        List<User> ls = new ArrayList<User>();
        for (int i = 0; i < numOfUsers * 2; i += 2) {
            User u = new User(strings.get(i + 1), InetAddress.getByName(strings.get(i)));
            ls.add(u);
        }
        return ls;
    }

    // asks the GUI for a name and
    // tries the name out on the server.
    // it responds the result to the GUI with nameRegistrationResponse(String msg)
    // It keeps asking for a name until the the app is to be closed or
    // a name could be found.
    private void registerName() {
        while (registeredUsername == null && storageServices.isRunning()) {
            String userName = storageServices.popUserName();    // blokierender Aufruf der auf den UserNamen wartet.
            println(new_(userName));    // send to the server
            System.out.println("Sent: " + userName);
            List<String> tokens;
            try {
                tokens = readFromServer();
                System.out.println("Recv: " + tokens);
                if (isSucess(tokens)) {  // if the registration succeeded
                    registeredUsername = userName;  // save the username. this will also end the loop.
                    guiServices.nameRegistrationResponse(OK);       // tell the gui, that it was successful.
                } else {
                    guiServices.nameRegistrationResponse(tokens.get(1));    // tell the gui that it failed and start from beginning of loop
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
                storageServices.stop();
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

    // flushenddes Println(genauso wie bei Server)
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
