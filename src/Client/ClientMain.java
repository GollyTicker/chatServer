package Client;

import Client.GUI.ClientGUI;
import Client.ThreadSafeData.Storage;
import utils.ClientProtocol;
import utils.ServerProtocol;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ClientMain {

    public static void main(String[] args) {

        Storage storage = new Storage();    // thread-safe Storage anschmeißen

        //GUIThread guiThread = new GUIThread(storage);
        ClientGUI gui = new ClientGUI(storage);  // die GUI hat Zugriff auf die Daten

        // Der TCP Thread hat Zugriff auf die Daten und auf Services der GUI
        TCPThread tcp = new TCPThread(ServerProtocol.TCP_HOSTNAME, ServerProtocol.TCP_PORT, gui, storage);

        // Der UDP thread
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT, storage);

        // starten aller Threads
        //guiThread.start();
        tcp.start();
        udp.start();
    }

}
