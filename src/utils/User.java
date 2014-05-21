package utils;

import java.net.InetAddress;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class User {
    public String name;
    public InetAddress host;

    public User(String userName, InetAddress userAddress) {
        name = userName;
        host = userAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        try {
            return ((User) o).name.equals(name);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return name + host;
    }
}
