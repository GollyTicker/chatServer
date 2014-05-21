package utils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ClientProtocol {

    public static final int UDP_PORT = 50000;

    public static final String INFO = "INFO";
    public static final String BYE = "BYE";
    public static final String NEW = "NEW";
    public static final String NEWLINE = "\r\n";
    public static final String SPACE = " ";
    public static final int CLIENT_INFO_WAIT_MS = 3500; // millis to wait for sending INFO requests

    public static String new_(String userName) {
        return NEW + SPACE + userName;
    }

    public static String info() {
        return INFO;
    }

    public static String bye() {
        return BYE;
    }

    // is a response a success?
    public static boolean isSucess(List<String> ls) {
        return ls.get(0).equals(ServerProtocol.OK);
    }
}
