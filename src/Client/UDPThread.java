package Client;

import Client.GUI.GUIServices;
import Client.ThreadSafeData.StorageServices;
import utils.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class UDPThread extends Thread {
    StorageServices storageServices;
    int udpPort;
    GUIServices gui;

    public UDPThread(int udpPort, StorageServices storage, GUIServices gui) {

        this.udpPort = udpPort;
        this.storageServices = storage;
        this.gui = gui;
    }

    @Override
    public void run() {
        System.out.println("UDP Thread started.");
        while (inAndOutRun()) {

        }

    }

    class Reciever extends Thread {


        //addchatmessage
        //refresh chat message
    }




    class Sender extends Thread {
        public Sender() {
        }
        @Override
        public void run() {
            System.out.println("something started");
            while (storageServices.isRunning()) {
                String input = storageServices.popChatLine();
                if (input == null) break;
                sendToAll(input);
            }
        }

        private void sendToAll(String input) {
            byte[] data = input.getBytes();
            for (User u : storageServices.getUserList()) {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    DatagramPacket output = new DatagramPacket(data,
                            data.length, u.host, udpPort);
                    socket.send(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    private boolean inAndOutRun() {
        return false;
    }


}
