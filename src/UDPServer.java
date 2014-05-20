import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Swaneet on 19.05.2014.
 */
public class UDPServer {
    public static void main(String[] args) throws IOException {


        DatagramSocket socket = new DatagramSocket(9876);

        boolean running = true;

        while (running) {

            byte[] sendData = new byte[1024];
            byte[] recvData = new byte[1024];

            DatagramPacket recvPkt = new DatagramPacket(recvData, recvData.length);
            socket.receive(recvPkt);
            String recv = new String(recvPkt.getData(), "UTF-8");
            System.out.println("Received: " + recv);


            InetAddress address = recvPkt.getAddress();
            int destPort = recvPkt.getPort();
            String send = recv.toUpperCase();
            sendData = send.getBytes("UTF-8");

            DatagramPacket sendPkt = new DatagramPacket(sendData, sendData.length, address, destPort);
            socket.send(sendPkt);
            System.out.println("Sent: " + send);
            if (recv.startsWith("exit")) {
                running = false;
            }
        }
        socket.close();
        System.out.println("Closed socket.");
    }
}
