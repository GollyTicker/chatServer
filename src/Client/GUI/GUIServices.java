package client.GUI;

import client.ChatMsg;
import utils.User;

import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface GUIServices {
    // methoden, die die GUI den TCP/UDP threads anbietet

    public String getUserName();    // blockierender Aufruf der solange wartet bis ein Username eingegeben wurde

    public String readChatLine();  // blokierender Aufruf welcher die nächste eingegebene Zeile einliest

    public void nameRegistrationResponse(String msg);   // der TCP thred kann hier die Antwort auf die Anfrage versenden

}
