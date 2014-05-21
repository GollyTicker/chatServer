package server;

import utils.ServerProtocol;
import utils.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerMain {

    static ServerSocket welcomeSocket;

    public static List<User> activeUsers = new LinkedList<User>();

    private ServerMain() {
    }

    public void serverRun() throws IOException {
        welcomeSocket = new ServerSocket(ServerProtocol.TCP_PORT);
        while (true) {
            handleNewClients();
        }
    }

    public static void main(String[] args) throws IOException {
        new ServerMain().serverRun();
    }

    private void handleNewClients() throws IOException {
        try {
            Socket clientSocket = welcomeSocket.accept();
            new ServerThread(clientSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
