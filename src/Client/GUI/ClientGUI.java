package Client.GUI;

import Client.ChatMsg;
import Client.ThreadSafeData.StorageServices;
import utils.ServerProtocol;
import utils.User;

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
    // for the chat room
    private JTextField chatInputArea;

    private JTextArea activeUsers;

    JPanel loginPanel;

    private StorageServices storageServices;

    public ClientGUI(final StorageServices storageServices) {
        super("Chabo Chat");

        setSize(600, 200);

        this.storageServices = storageServices;


        //
        userNameLabel = new JLabel("Username", SwingConstants.CENTER);

        //default value
        userNameTextField = new JTextField("");
        userNameTextField.setColumns(20);
        userNameTextField.setBackground(Color.WHITE);

        // login button
        login = new JButton("Login");
        login.addActionListener(this);


        loginPanel = new JPanel();
        loginPanel.add(userNameLabel, BorderLayout.NORTH);
        loginPanel.add(userNameTextField, BorderLayout.CENTER);
        loginPanel.add(login, BorderLayout.SOUTH);
        add(loginPanel, BorderLayout.CENTER);

        userNameTextField.addActionListener(this);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        userNameTextField.requestFocusInWindow();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                storageServices.stop();
                try {
                    Thread.sleep(300);
                }
                catch (InterruptedException q) {}
            }
        });
    }

    private void switchToChatMode() {

        //set login panel invisible
        loginPanel.setVisible(false);
        loginPanel.removeAll();

        JPanel centerPanel = new JPanel();
        chatTextArea = new JTextArea("Chat", 15, 30);
        chatTextArea.setColumns(30);
        chatTextArea.setRows(15);
        chatTextArea.setText("=== Connected ===" + "\n");
        chatTextArea.setEditable(false);
        activeUsers = new JTextArea("");
        activeUsers.setColumns(15);
        activeUsers.setRows(15);
        activeUsers.setEditable(false);
        centerPanel.add(activeUsers);
        centerPanel.add(new JScrollPane(chatTextArea), BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridBagLayout());
        chatInputArea = new JTextField("say hello");
        chatInputArea.setColumns(35);
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(this);
        southPanel.add(chatInputArea);
        southPanel.add(sendMessage);

        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        revalidate(); // force redraw frames

        chatInputArea.requestFocusInWindow();
        chatInputArea.selectAll();
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
            ChatMsg chatMsg = new ChatMsg(storageServices.latestUserName(), chatInputArea.getText());
            storageServices.putChatLine(chatMsg);
            chatInputArea.setText("");
            chatInputArea.requestFocusInWindow();
            // System.out.println("Entered: " + chatInputArea.getText());
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

    @Override
    public void refreshUserList() {
        activeUsers.setText("");
        for (User u:storageServices.getUserList()){
            activeUsers.append(u.name + "\n");
        }
    }
}
