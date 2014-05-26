package utils;

import Server.ServerMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerProtocol {

    public static final int TCP_PORT = 50000;

    public static final String TCP_HOSTNAME = "lab25";

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public static final String BYE = "BYE";
    public static final String LIST = "LIST";
    public static final String NEWLINE = "\r\n";
    public static final String SPACE = " ";

    // implements the list response
    public static String list() {
        List<User> users = ServerMain.getImmutableCopyOfUsers();
        int numberOfUsers = users.size();
        StringBuilder sb = new StringBuilder();
        for (User u:users) {
            sb.append(SPACE);
            sb.append(u.toString());
        }
        return LIST + SPACE +  numberOfUsers + sb.toString();
    }

    // implements the ok response
    public static String ok() {
        return OK;
    }

    // implements the error response
    public static String error(String message) {
        return (ERROR + SPACE + message);
    }

    // implements the bye response
    public static String bye() {
        return BYE;
    }

    // turns a read line into space separated tokens.
    // f.e. "NEW IvanMorozov slkdflskdf blubb hahe" => ["NEW", "IvanMorozov", "slkdflskdf", "blubb", "hahe"]
    public static List<String> tokenize(String input) {
        String[] a = input.split(" ");
        List<String> s = new ArrayList<String>();
        for(String str:a){
            s.add(str);
        }
        return s;
    }



}
