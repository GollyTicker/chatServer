package utils;

import server.ServerMain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ServerProtocol {

    public static final int TCP_PORT = 50000;

    public static final String TCP_HOSTNAME = "localhost";

    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

    public static final String BYE = "BYE";
    public static final String LIST = "LIST";
    public static final String NEWLINE = "\r\n";
    public static final String SPACE = " ";

    public static String list(List<User> ls) {
        int numberOfUsers = ServerMain.activeUsers.size();
        return LIST + SPACE +  numberOfUsers + SPACE + "(hier die users und dessen hosts)";
    }

    public static String ok() {
        return OK;
    }

    public static String error(String message) {
        return (ERROR + SPACE + message);
    }

    public static String bye() {
        return BYE;
    }

    public static List<String> tokenize(String input) {
        String[] a = input.split(" ");
        List<String> s = new ArrayList<String>();
        for(String str:a){
            s.add(str);
        }
        return s;
    }

    public static boolean isSucess(List<String> ls) {
        return ls.get(0).equals(OK);
    }



}
