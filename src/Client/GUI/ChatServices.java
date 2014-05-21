package client.GUI;

import client.ChatMsg;
import utils.User;

import java.util.List;

/**
 * Created by Swaneet on 20.05.2014.
 */
public interface ChatServices {
    // methoden, die die TCP, UDP threads der GUI anbieten.

    public List<ChatMsg> getMesseages();

    public List<User> getUserList();
}
