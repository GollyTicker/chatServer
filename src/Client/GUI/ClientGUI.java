package Client.GUI;

import Client.ChatMsg;
import Client.ThreadSafeData.StorageServices;
import utils.ServerProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener, GUIServices {


    // laber for the Username field description
    private JLabel userNameLabel;
    //testfield for enter the username
    private JTextField userNameTextField;
    //buttons
    private JButton login, sendMessage;

    private volatile String userNameStr = "";

    // for the chat room
    private JTextArea chatTextArea;

    JPanel loginPanel;

    private StorageServices storageServices;

    public ClientGUI(final StorageServices storageServices) {
        super("Chabo Chat");

        setSize(400, 400);

        this.storageServices = storageServices;


        //
        userNameLabel = new JLabel("Username", SwingConstants.CENTER);

        //default value
        userNameTextField = new JTextField("");
        userNameTextField.setSize(30,10);

        userNameTextField.setBackground(Color.BLUE);

        // login button
        login = new JButton("Login");
        login.addActionListener(this);


        loginPanel = new JPanel();
        loginPanel.add(userNameLabel);
        loginPanel.add(userNameTextField);
        loginPanel.add(login);
        add(loginPanel, BorderLayout.CENTER);

        // the chat room
        // switchToChatMode();

        userNameTextField.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        userNameTextField.requestFocusInWindow();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storageServices.stop();
            }
        });
    }

    private void switchToChatMode() {

        //set login panel invisibles
        loginPanel.removeAll();
        loginPanel.setVisible(false);

        JPanel centerPanel = new JPanel();
        chatTextArea = new JTextArea("Chat", 20, 30);
        chatTextArea.setText("=== Connected ===" + "\n");
        chatTextArea.setEditable(false);

        sendMessage = new JButton("Send");
        sendMessage.addActionListener(this);

        centerPanel.add(sendMessage);
        centerPanel.add(new JScrollPane(chatTextArea), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.NORTH);

        revalidate();
    }

    @Override
    public void refreshChatMessages() {
        for(ChatMsg msg:storageServices.getLatestMessages()){
            chatTextArea.append(msg.toString() + "\n");
        }
    }

    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o == login) {
            storageServices.putUserName(userNameTextField.getText());
            return;
        }
        if (o == sendMessage) {
            System.out.println("Not implemented!");
            return;
        }
    }

    @Override
    public void nameRegistrationResponse(String msg) {
        if(msg.equals(ServerProtocol.OK)) {
            switchToChatMode(); // if registration successful, then switch to chat mode
        }
        else {
            userNameLabel.setText(msg); // else display the error
        }
    }
}
