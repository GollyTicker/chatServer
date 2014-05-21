package Client.GUI;

import Client.ChatMsg;
import utils.User;

import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface StorageServices {
    // methoden, die die Speicherung von synchronisierten Daten zwishcen den TCP/UDP threads und der GUI anbieten.

    public List<ChatMsg> getMesseages();

    public List<User> getUserList();

    public void saveUserList(List<User> users);

    public boolean isRunning();

    public void stop();
}
