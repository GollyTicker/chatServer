package server;

import java.io.*;
import java.net.Socket;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerThread extends Thread {
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public ServerThread(Socket clientSocket) {
        socket = clientSocket;
    }

    @Override
    public void run() {
        initReaderWriter();
    }

    private void initReaderWriter() {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
        }
    }
}
