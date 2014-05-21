package client;

import client.GUI.GUIThread;
import utils.ClientProtocol;
import utils.ServerProtocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ClientMain {

    public static void main(String[] args) {
        GUIThread guiThread = new GUIThread();
        TCPThread tcp = new TCPThread(ServerProtocol.TCP_HOSTNAME, ServerProtocol.TCP_PORT);
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT);
        guiThread.start();
        tcp.start();
        udp.start();
    }

}
