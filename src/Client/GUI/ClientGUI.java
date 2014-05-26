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
    private static final int userNameTextFieldChars = 20;
    //buttons
    private JButton login, sendMessage;

    private volatile String userNameStr = "";

    // for the chat room
    private JTextArea chatTextArea;
    private static final int chatTextAreaColumns = 30;
    private static final int chatTextAreaRows = 15;
    // for the chat room
    private JTextField chatInputArea;
    private static final int chatInputAreaChars = 35;

    private JTextArea activeUsers;
    private static final int activeUsersColumns = 20;
    private static final int activeUsersRows = 15;

    JPanel loginPanel;

    private StorageServices storageServices;

    private static final int centerW = 200;
    private static final int centerH = 300;

    private static final int windowW = 700;
    private static final int windowH = 300;

    private static final long THREAD_GUI_WAIT_MS = 300;

    public ClientGUI(final StorageServices storageServices) {
        super("Chabo Chat");

        setSize(windowW, windowH);

        this.storageServices = storageServices;


        //
        userNameLabel = new JLabel("Username", SwingConstants.CENTER);

        //default value
        userNameTextField = new JTextField("");
        userNameTextField.setColumns(userNameTextFieldChars);
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
                System.out.println("Quitting...");
                storageServices.guiQuittable();
                try {
                    Thread.sleep(THREAD_GUI_WAIT_MS);
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
        centerPanel.setSize(centerW, centerH);
        chatTextArea = new JTextArea("Chat", 1, 1);
        chatTextArea.setColumns(chatTextAreaColumns);
        chatTextArea.setRows(chatTextAreaRows);
        chatTextArea.setText("=== Connected ===" + "\n");
        chatTextArea.setEditable(false);
        activeUsers = new JTextArea("");
        activeUsers.setColumns(activeUsersColumns);
        activeUsers.setRows(activeUsersRows);
        activeUsers.setEditable(false);
        centerPanel.add(activeUsers);
        centerPanel.add(new JScrollPane(chatTextArea), BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridBagLayout());
        chatInputArea = new JTextField("say hello");
        chatInputArea.setColumns(chatInputAreaChars);
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
            activeUsers.append(u + "\n");
        }
    }
}
