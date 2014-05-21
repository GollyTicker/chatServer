package utils;

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
        return LIST + SPACE +  "hier die users und dessen hosts";
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



}
