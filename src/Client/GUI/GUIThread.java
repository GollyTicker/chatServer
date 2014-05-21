package Client.GUI;

import Client.ThreadSafeData.Storage;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class GUIThread extends Thread implements GUIServices {

    public GUIThread(Storage storage) {

    }

    @Override
    public void run() {

    }

    @Override
    public String getUserName() {
        // TODO: wait until user enters a name.
        return "IvanMorozov";
    }

    @Override
    public String readChatLine() {  // not called yet!
        // TODO: wait until the user enters a new chatline
        return "I wrote a line here!";
    }

    @Override
    public void nameRegistrationResponse(String msg) {

    }

}
