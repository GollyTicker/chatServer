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
        initReaderPrinter();
        while (clientConnected()) {
            try {
                String recv = reader.readLine();
                List<String> tokens = tokenize(recv);
                String command = tokens.get(0);
                String send = error("Unknown Command");
                if (tokens.size() > 0) {
                    if (command.equals(ClientProtocol.NEW)) {
                        if (user != null) {  // if user is already defined
                            send = error("You have already registered as " + user.name);
                        } else if (tokens.size() == 2 && isValidUsername(tokens.get(1))) {
                            String userName = tokens.get(1);
                            InetAddress userAddress = socket.getInetAddress();
                            // TODO: check if user already exists in the list
                            user = new User(userName, userAddress);
                            ServerMain.activeUsers.add(user);
                            send = ok();
                        } else { // if no name was given or it contained space characters
                            send = error("Invalid Input");
                        }
                    } else if (command.equals(ClientProtocol.BYE)) {
                        ServerMain.activeUsers.remove(user);
                        send = bye();
                        receivedBYE = true;
                    } else if (command.equals(ClientProtocol.INFO)) {
                        send = list(ServerMain.activeUsers);
                    }
                }

                System.out.println("Sending: " + send);
                println(send);
            } catch (IOException e) {
                e.printStackTrace();
                receivedBYE = true;
            }
        }
        closeAll();
    }

    private void println(String str) {
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
        return s.matches("[A-Za-z0-9]{1,12}");
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
