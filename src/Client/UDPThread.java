package client;

import client.GUI.StorageServices;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class UDPThread extends Thread {
    StorageServices storageServices;
    public UDPThread(int udpPort, StorageServices storage) {
        this.storageServices = storage;
    }

    @Override
    public void run() {

    }
}
