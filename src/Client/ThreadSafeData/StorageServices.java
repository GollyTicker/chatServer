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

    // die GUI kann sich hier die Messages aus der Queue holen
    public List<ChatMsg> getLatestMessages();

    // the UDP thread can add new Messages here
    public void addChatMessage(ChatMsg msg);

    // die GUI kann sich hier die Liste der aktuell registrierten User holen
    public List<User> getUserList();

    // der TCP-Thread kann hiermit die vom Server geholte userList aktualisieren
    public void saveUserList(List<User> users);

    // alle Threads können hiermit wissen,
    // ob sie weiterarbeiten sollen oder nicht.
    public boolean isRunning();

    public void putUserName(String userName);
    public String popUserName();

    // alle Thread können hiermit die App schließen.
    // Z.b. wenn ein unerwarteter Fehler im TCP Thread kommt.
    // z.B. wenn der Anwender die GUI schließt.
    public void stop();

    public void putChatLine(String message);
    public String popChatLine();
}
