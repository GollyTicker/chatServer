package Client.GUI;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface GUIServices {
    // methoden, die die GUI den TCP/UDP threads anbietet

    public String getUserName();    // blockierender Aufruf der solange wartet bis ein Username eingegeben wurde

    public void nameRegistrationResponse(String msg);   // der TCP Thread kann hier die Antwort auf die Anfrage versenden

}
