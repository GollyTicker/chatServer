package Server;

import utils.ServerProtocol;
import utils.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerMain {

    static ServerSocket welcomeSocket;

    // diese Methoden hier gibt es um die Liste der aktuell registrierten User festzuhalten.
    // die UserList wird nur von hier aus zugegriffen. und evtl verändert.
    private static volatile List<User> activeUsers = new LinkedList<User>();
    public static synchronized void removeUser(User u) {
        activeUsers.remove(u);
    }
    public static synchronized void addUser(User u) {
        activeUsers.add(u);
    }
    public static synchronized List<User> getImmutableCopyOfUsers() {
        return new ArrayList<User>(activeUsers);
    }

    private ServerMain() {
    }

    public void serverRun() throws IOException {
        // default users
        /*
        activeUsers.add(new User("Hadsfsd", getByName("localhost")));
        activeUsers.add(new User("Blubb", getByName("haw-hamburg.de")));
        activeUsers.add(new User("Denkte", getByName("desy.de")));
        */

        // beim start des Servers wird das TCP socket angelegt und öffnen
        // für jeden Client einen neuen Thread
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
            System.out.println(e.getMessage());
        }
    }

}
