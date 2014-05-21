package client;

import client.GUI.GUIThread;
import utils.ClientProtocol;
import utils.ServerProtocol;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ClientMain {

    public static void main(String[] args) {
        Thread guiThread = new Thread(new GUIThread());
        TCPThread tcp = new TCPThread(ServerProtocol.TCP_HOSTNAME, ServerProtocol.TCP_PORT);
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT);
        guiThread.start();
    }

}
