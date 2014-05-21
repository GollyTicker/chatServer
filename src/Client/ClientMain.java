package Client;

import Client.GUI.GUIThread;
import utils.ClientProtocol;
import utils.ServerProtocol;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ClientMain {

    public static void main(String[] args) {
        Storage storage = new Storage();
        GUIThread guiThread = new GUIThread(storage);
        TCPThread tcp = new TCPThread(ServerProtocol.TCP_HOSTNAME, ServerProtocol.TCP_PORT, guiThread, storage);
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT, storage);
        guiThread.start();
        tcp.start();
        udp.start();
    }

}
