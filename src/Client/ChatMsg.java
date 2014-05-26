package Client;

import utils.ClientProtocol;
import utils.ServerProtocol;

/**
 * Created by Swaneet on 20.05.2014.
 */
public class ChatMsg {
    // stellt eine einzige Chat Zeile dar.

    public String name;
    public String msg;

    public ChatMsg(String name, String rawMsg) {
        this.name = name;
        String singleLineMsg = rawMsg.replace('\n', ' ');  // darf keine NEWLINES enthalten
        String finalMsg = "";
        // darf hoechstens 100 Zeichen lang sein.
        if(singleLineMsg.length() < ClientProtocol.MAX_CHATMSG_LENGTH) {
            finalMsg = singleLineMsg;
        }
        else {
            finalMsg = singleLineMsg.substring(0,ClientProtocol.MAX_CHATMSG_LENGTH-1);
        }
        this.msg = finalMsg;
    }

    @Override
    public String toString() {
        return name  + ClientProtocol.CHAT_MSG_SEPERATOR + ServerProtocol.SPACE + msg;
    }
}
