/**
 * Created by Swaneet on 19.05.2014.
 */

import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        boolean running = true;

        DatagramSocket socket = new DatagramSocket();   // SocketException
        while (running) {

            InetAddress address = InetAddress.getByName("localhost");   // UnknownHostException
            int port = 9876;

            byte[] sendData = new byte[1024];
            byte[] recvData = new byte[1024];

            String line = keyboard.readLine();  // IOException

            sendData = line.getBytes("UTF-8");

            DatagramPacket sendPkt = new DatagramPacket(sendData, sendData.length, address, port);

            socket.send(sendPkt);
            System.out.println("Sent: " + line);

            DatagramPacket recvPkt = new DatagramPacket(recvData, recvData.length);

            socket.receive(recvPkt);
            String resp = new String(recvPkt.getData(), "UTF-8");
            System.out.println("Received: " + resp);

            if (line.startsWith("exit")) {
                running = false;
            }
        }
        socket.close();
    }
}
