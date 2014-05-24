package Client.GUI;

import Client.ThreadSafeData.StorageServices;
import utils.ServerProtocol;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

    public ClientGUI(StorageServices storageServices) {
        super("Chabo Chat");

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
        setSize(600, 600);
        setVisible(true);
        userNameTextField.requestFocus();
    }

    private void switchToChatMode() {

        //set login panel invisibles
        loginPanel.removeAll();
        loginPanel.setVisible(false);

        chatTextArea = new JTextArea("=== Connected ===\n", 80, 80);
        chatTextArea.setVisible(false);
        JPanel centerPanel = new JPanel(new GridLayout(1, 1));
        chatTextArea.setEditable(false);

        sendMessage = new JButton("Send");
        sendMessage.setVisible(false);
        sendMessage.addActionListener(this);

        centerPanel.add(sendMessage);
        centerPanel.add(new JScrollPane(chatTextArea));
        add(centerPanel, BorderLayout.CENTER);
    }

    //
    void append(String str) {
        chatTextArea.append(str);
        chatTextArea.setCaretPosition(chatTextArea.getText().length() - 1);
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
