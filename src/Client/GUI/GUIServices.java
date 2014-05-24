package Client.GUI;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface GUIServices {
    // methoden, die die GUI den TCP/UDP threads anbietet

    public void refreshChatMessages();

    public void nameRegistrationResponse(String msg);   // der TCP Thread kann hier die Antwort auf die Anfrage versenden

}
