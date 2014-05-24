package Client.GUI;

import Client.ThreadSafeData.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        //return "IvanMorozov";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            return br.readLine();
        } catch (IOException e) {
            return "IvanMorozov";
        }
    }

    @Override
    public void nameRegistrationResponse(String msg) {

    }

}
