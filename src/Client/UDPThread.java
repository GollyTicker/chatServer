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
        Reciever r = new Reciever();
        Sender s = new Sender();

        r.start();
        s.start();

        while (r.isAlive() && s.isAlive()) {
            if (!storageServices.isRunning()) {
                break;
            }
        }

    }

    class Reciever extends Thread {


        public Reciever() {
        }

        @Override
        public void run() {
            try {
                DatagramSocket ds = new DatagramSocket(udpPort);
                while (storageServices.isRunning()) {
                    byte[] buffer = new byte[65507];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    ds.receive(dp);

                    String msg = new String(dp.getData(), 0, dp.getLength());
                    String userName = msg.split(":")[0];
                    String message = msg.split(":")[1];

                    storageServices.addChatMessage(new ChatMsg(userName,message));
                    gui.refreshChatMessages();

                }

            } catch (IOException se) {
                System.err.println("chat error " + se);
            }
        }


    }


    class Sender extends Thread {
        public Sender() {
        }

        @Override
        public void run() {
            System.out.println("something started");
            while (storageServices.isRunning()) {
                ChatMsg input = storageServices.popChatLine();
                System.out.println("der Input:"+ input);
                sendToAll(input);

            }
        }

        private void sendToAll(ChatMsg input) {
            byte[] data = input.toString().getBytes();
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


}
