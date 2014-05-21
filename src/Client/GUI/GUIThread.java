package client.GUI;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class GUIThread extends Thread implements GUIServices {

    @Override
    public void run() {

    }

    @Override
    public String getUserName() {
        // TODO: implement method
        return "IvanMorozov";
    }

    @Override
    public String readChatLine() {
        // TODO: implement method
        return "I wrote a line here!";
    }

    @Override
    public void nameRegistrationResponse(String msg) {

    }

}
