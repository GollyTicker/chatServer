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
        TCPThread tcp = new TCPThread(ServerProtocol.TCP_HOSTNAME, ServerProtocol.TCP_PORT, gui, storage);

        // Der UDP thread. UDP Thread needs to tell the GUI to get its messages.
        UDPThread udp = new UDPThread(ClientProtocol.UDP_PORT, storage,gui);

        // starten aller Threads
        //guiThread.start();
        tcp.start();
        udp.start();

        // Some Sample ChatMessages
        /*try {
            Thread.sleep(2500);
            storage.addChatMessage(new ChatMsg("Superman", "Hello World'! '' sdf :D"));
            gui.refreshChatMessages();
            Thread.sleep(500);
            storage.addChatMessage(new ChatMsg("IvanMorozov", "This is my second staement"));
            gui.refreshChatMessages();
            Thread.sleep(500);
            storage.addChatMessage(new ChatMsg("SwaneetSahoo", "Third statement!!"));
            gui.refreshChatMessages();
        } catch (InterruptedException e) {
        }*/
    }

}
