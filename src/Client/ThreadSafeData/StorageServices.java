package Client.ThreadSafeData;

import Client.ChatMsg;
import utils.User;

import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface StorageServices {
    // Die Storage Services erlauben TCP-Thread, UDP-Thread und GUI-Thread indirekt miteinander zu kommunizieren
    // Diese werden von Storage implementiert.

    // die GUI kann sich hier die Chat Nachrichten holen
    public List<ChatMsg> getMessages();

    // die GUI kann sich hier die Liste der aktuell registrierten User holen
    public List<User> getUserList();

    // der TCP-Thread kann hiermit die vom Server geholte userList aktualisieren
    public void saveUserList(List<User> users);

    // alle Threads können hiermit wissen,
    // ob sie weiterarbeiten sollen oder nicht.
    public boolean isRunning();

    // alle Thread können hiermit die App schließen.
    // Z.b. wenn ein unerwarteter Fehler im TCP Thread kommt.
    // z.B. wenn der Anwender die GUI schließt.
    public void stop();
}
