package client;

import client.GUI.StorageServices;
import utils.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Swaneet on 21.05.2014.
 */
public class Storage implements StorageServices {

    private static volatile List<ChatMsg> msgs = new ArrayList<ChatMsg>();
    private static volatile List<User> users = new ArrayList<User>();

    @Override
    public synchronized List<ChatMsg> getMesseages() {
        return msgs;
    }

    @Override
    public synchronized List<User> getUserList() {
        return users;
    }
}