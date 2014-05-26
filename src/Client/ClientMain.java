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

        ClientGUI gui = new ClientGUI(storage);  // die GUI hat Zugriff auf die Daten

        // Der TCP Thread hat Zugriff auf die Daten und auf Services der GUI
        TCPThread tcp = new TCPThread(ServerProtocol.SERVER_HOSTNAME, ServerProtocol.TCP_PORT, gui, storage);

        // Der UDP thread. UDP Thread needs to tell the GUI to get its messages.
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT, storage,gui);

        // starten aller Threads
        tcp.start();
        udp.start();
    }

}
