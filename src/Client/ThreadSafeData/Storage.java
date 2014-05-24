package Client.ThreadSafeData;

import Client.ChatMsg;
import utils.User;

import java.util.*;

/**
 * Created by Swaneet on 21.05.2014.
 */
public class Storage implements StorageServices {

    private static volatile boolean keepRunning = true;

    private static volatile Queue<ChatMsg> msgs = new LinkedList<ChatMsg>();
    private static volatile List<User> users = new ArrayList<User>();


    @Override
    public synchronized  List<ChatMsg> getLatestMessages() {
        List<ChatMsg> newMessages = new ArrayList<ChatMsg>();
        while (!msgs.isEmpty()) {
            newMessages.add(msgs.poll());
        }
        return newMessages;
    }

    @Override
    public synchronized void addChatMessage(ChatMsg msg) {
        msgs.add(msg);
    }

    @Override
    public synchronized boolean isRunning() {
        return keepRunning;
    }

    @Override
    public synchronized void stop() {
        keepRunning = false;
    }

    @Override
    public synchronized List<User> getUserList() {
        return users;
    }

    @Override
    public synchronized void saveUserList(List<User> users) {
        this.users = users;
    }
}
