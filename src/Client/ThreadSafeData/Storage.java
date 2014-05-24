package Client.ThreadSafeData;

import Client.ChatMsg;
import utils.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Swaneet on 21.05.2014.
 */
public class Storage implements StorageServices {

    private static volatile boolean keepRunning = true;

    private static volatile Queue<ChatMsg> msgs = new LinkedList<ChatMsg>();
    private static volatile List<User> users = new ArrayList<User>();
    private static volatile BlockingQueue<String> userNameHolder = new ArrayBlockingQueue<>(1);

    @Override
    public synchronized List<ChatMsg> getLatestMessages() {
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
    public void putUserName(String userName) {
        userNameHolder.add(new String(userName));
    }

    @Override
    public String popUserName() {

        try {
            return userNameHolder.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "abcd";
        }


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
