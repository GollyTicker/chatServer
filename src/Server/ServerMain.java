package server;

import utils.ServerProtocol;
import utils.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerMain {

    // ReadFcWriteFs stream;
    static ServerSocket welcomeSocket;

    public static Set<User> activeUsers;

    public ServerMain() {
        initializeServer();
        activeUsers = new HashSet<User>();
    }

    private void initializeServer() {
        try {
            welcomeSocket = new ServerSocket(ServerProtocol.TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNewClients() {
        try {
            Socket clientSocket = welcomeSocket.accept();
            new ServerThread(clientSocket).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serverRun() {
        while (true) {
            handleNewClients();
        }
    }

    public static void main(String[] args) {
        ServerMain s = new ServerMain();
        s.serverRun();
    }

}
