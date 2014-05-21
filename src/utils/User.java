package utils;

import java.net.InetAddress;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class User {
    public static String name;
    public static InetAddress host;
    public User(String userName, InetAddress userAddress) {
        name = userName;
        host = userAddress;
    }
}
