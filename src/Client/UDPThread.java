package Client;

import Client.GUI.GUIServices;
import Client.ThreadSafeData.StorageServices;
import utils.ClientProtocol;
import utils.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class UDPThread extends Thread {
    private static final int UTF_CHAT_MSG_BYTESIZE = 8*(20 + 1 + 1 + 100); // jeder UFT Char kann bis zu 8 Bytes lang sein
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
                    byte[] buffer = new byte[UTF_CHAT_MSG_BYTESIZE];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    ds.receive(dp);

                    String msg = new String(dp.getData(), 0, dp.getLength(),"UTF-8");
                    System.out.println("Reveiced over UDP: " + msg);
                    try {
                        String userName = msg.split(ClientProtocol.CHAT_MSG_SEPERATOR)[0];
                        String message = msg.split(ClientProtocol.CHAT_MSG_SEPERATOR)[1];
                        storageServices.addChatMessage(new ChatMsg(userName, message));
                        gui.refreshChatMessages();
                    } catch (ArrayIndexOutOfBoundsException e) {    // if input was malformed
                        System.out.println("Invalid UDP Input:" + msg);
                    }
                }
                System.out.println("UDP Thread ended.");
            } catch (IOException se) {
                System.err.println("Generic Error " + se);
            }
        }


    }


    class Sender extends Thread {
        public Sender() {
        }

        @Override
        public void run() {
            while (storageServices.isRunning()) {
                ChatMsg input = storageServices.popChatLine();
                System.out.println("Der Input:" + input);
                sendToAll(input);

            }
        }

        private void sendToAll(ChatMsg input) {
            byte[] data = new byte[UTF_CHAT_MSG_BYTESIZE];
            try {
                data = input.toString().getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                data = input.toString().getBytes();
            }
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
