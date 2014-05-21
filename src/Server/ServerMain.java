package server;

import utils.ServerProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerMain {

    // ReadFcWriteFs stream;
    static ServerSocket welcomeSocket;

    public ServerMain() {
        initializeServer();
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
