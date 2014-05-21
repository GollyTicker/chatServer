package Server;

import utils.ClientProtocol;
import utils.User;

import static utils.ServerProtocol.*;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerThread extends Thread {
    Socket socket;
    BufferedReader reader;
    PrintWriter printer;
    private boolean receivedBYE = false;
    private User user;

    public ServerThread(Socket clientSocket) {
        socket = clientSocket;
    }

    @Override
    public void run() {

        System.out.println("New Thread at time: " + System.currentTimeMillis());

        // zu Beginn werden die IO Streams initialisiert
        initReaderPrinter();

        // solange der Client noch verbunden ist und noch kein BYE gesendet hat
        // werden die Commandos NEW, INFO und BYE verarbeitet
        while (clientConnected()) {
            try {
                List<String> tokens = tokenize(reader.readLine());
                String command = tokens.get(0);
                String send = error("Unknown Command"); // standard response if no command was recognzied.

                // ******************* Main Commands ***************************
                if (tokens.size() > 0) {    // make sure the input wasnt only spaces
                    if (command.equals(ClientProtocol.NEW)) {
                        // if the command is NEW, then tryout the new User
                        send = tryNewUser(tokens);
                    } else if (command.equals(ClientProtocol.BYE)) {
                        // if the command is bye, then remove my user and mark the end for the loop.
                        ServerMain.removeUser(user);
                        send = bye();
                        receivedBYE = true;
                    } else if (command.equals(ClientProtocol.INFO)) {
                        // if the command is info, then make userList
                        send = list(ServerMain.activeUsers);
                    }
                }
                // ******************* END Main Commands ***************************

                println(send);
            } catch (IOException e) {   // if anything happens in an iteration, print it out and end the loop.
                System.out.println(e.getMessage());
                receivedBYE = true;
            }
        }
        // after the loop was finished (either by Exception or BYE command), remove the user (just to be safe)
        // and close various resources.
        ServerMain.removeUser(user);
        closeAll();
    }

    private String tryNewUser(List<String> tokens) {
        if (user != null) {  // if user is already defined
            return error("You have already registered as " + user.name);
        } else if (tokens.size() == 2 && isValidUsername(tokens.get(1))) {
            if (!ServerMain.activeUsers.contains(tokens.get(1))) {  // make sure the user doesnt already exist
                user = new User(tokens.get(1), socket.getInetAddress());    // create the user and store it in the list
                ServerMain.addUser(user);
                return ok();
            } else {
                return error(tokens.get(1) + " already used.");
            }
        } else { // if no name was given or it contained space characters
            return error("Invalid Username" + tokens);
        }
    }

    // wrapper around "printer.readLine()"
    // forcing flush everytime to send the message over network.
    // also allows us to write debug statements here.
    private void println(String str) {
        System.out.println("To " + user + " send:" + str);
        printer.println(str);
        printer.flush();
    }

    private void closeAll() {
        try {
            reader.close();
            printer.close();
            socket.close();
        } catch (IOException e) {
        }
    }

    private boolean isValidUsername(String s) {
        return s.matches("[A-Za-z0-9]{1,35}");
    }

    private boolean clientConnected() {
        return socket.isConnected() && !receivedBYE;
    }

    private void initReaderPrinter() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            printer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
        }
    }
}
