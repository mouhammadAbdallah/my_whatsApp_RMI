/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import mywhatsapp.User.Contact;
import mywhatsapp.User.Group;

/**
 *
 * @author Salah
 */
public class ClientGui extends javax.swing.JFrame {

    /**
     * Creates new form ClientGui
     */
    public String Id;
    IMyWhatsappServer serverRef;
    MyWhatsappClient client;
    String username;
    public User me;
    public ArrayList<String> specialUser = new ArrayList();

    public ClientGui(String Id) throws NotBoundException, MalformedURLException {
        this.Id = Id;
        initComponents();
        setVisible(true);

        try {
            serverRef = (IMyWhatsappServer) Naming.lookup(MyWhatsappClient.RMIRef);
            client = new MyWhatsappClient();
            me = serverRef.connect(Id, client);
            username = me.name;
            for (Contact con : me.contacts.values()) {
                if (!con.offlineChat.isEmpty()) {
                    specialUser.add(con.Id);
                }
            }
            for (Group gp : me.groups.values()) {
                if (!gp.offlineChat.isEmpty()) {
                    specialUser.add(gp.groupId);
                }
            }
            Refresh();
            me.ReadOffLineMessageOfAllContactsAndGroups();
            serverRef.ReadOffLineMessageOfAllContacts(Id);

        } catch (RemoteException ex) {
            Logger.getLogger(ClientGui.class.getName()).log(Level.SEVERE, null, ex);
        }

        myIdLbl.setText(Id);
        myUserNameLbl.setText(username);

    }

    ArrayList<JButton> contactsBtn = new ArrayList();
    int i = -1;

    public void Refresh() {

        while (i >= 0) {
            this.remove(contactsBtn.get(i));
            contactsBtn.remove(i);
            this.validate();
            i--;
        }
        for (Contact con : me.contacts.values()) {
            i++;
            JButton contactBtn = new JButton(con.name);
            contactBtn.setFont(new java.awt.Font("sansserif", 0, 20));
            contactBtn.setBounds(5, 120 + 60 * i, 230, 60);
            contactBtn.setBackground(Color.WHITE);
            contactBtn.setName(con.Id.trim());
            int nbOfmessage = 0;
            if (specialUser.contains(con.Id.trim()) && !recieverId.equals(con.Id.trim())) {
                contactBtn.setFont(new Font("sansserif", 1, 20));
                String[] name = contactBtn.getText().split("\\(");
                if (name.length == 1) {
                    if (con.offlineChat.size() != 0) {
                        nbOfmessage = con.offlineChat.size();
                        contactBtn.setText(name[0] + "(" + nbOfmessage + ")");
                    }
                }
            }
            contactBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    chatBtnActionPerformed(evt);
                }
            });
            this.add(contactBtn);
            this.validate();
            contactsBtn.add(contactBtn);
        }

        for (Group gp : me.groups.values()) {
            i++;
            JButton contactBtn = new JButton(gp.groupName);
            contactBtn.setFont(new java.awt.Font("sansserif", 0, 20));
            contactBtn.setBounds(5, 120 + 60 * i, 230, 60);
            contactBtn.setBackground(Color.WHITE);
            contactBtn.setName(gp.groupId.trim());
            int nbOfmessage = 0;
            if (specialUser.contains(gp.groupId.trim()) && !recieverId.equals(gp.groupId.trim())) {
                contactBtn.setFont(new Font("sansserif", 1, 20));
                String[] name = contactBtn.getText().split("\\(");
                if (name.length == 1) {
                    if (gp.offlineChat.size() != 0) {
                        nbOfmessage = gp.offlineChat.size();
                        contactBtn.setText(name[0] + "(" + nbOfmessage + ")");
                    }
                }
            }
            contactBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    chatBtnActionPerformed(evt);
                }
            });
            this.add(contactBtn);
            this.validate();
            contactsBtn.add(contactBtn);
        }

        if (!recieverId.isEmpty()) {
            chatPnl.removeAll();
            String contactId = recieverId;
            recieverId = contactId;
            if (recieverId.contains("wa")) {
                ArrayList<Contact.MessageItem> chat = me.contacts.get(contactId).onlineChat;
                int i = -1;
                for (Contact.MessageItem msg : chat) {
                    i++;
                    String message = msg.message.message.toString();
                    int messageLn = message.length() * 12;
                    String time = msg.sendingTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    JLabel messageLbl = new JLabel();
                    messageLbl.setFont(new Font(messageLbl.getFont().getName(), Font.PLAIN, 18));
                    JLabel timeLbl = new JLabel();
                    timeLbl.setFont(new Font(timeLbl.getFont().getName(), Font.PLAIN, 10));
                    messageLbl.setOpaque(true);
                    timeLbl.setOpaque(true);
                    messageLbl.setText(message);
                    timeLbl.setText(time);
                    if (msg.senderId.equals(Id)) {
                        messageLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 45 * i, messageLn, 30);
                        timeLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 30 + 45 * i, 35, 10);
                        messageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                        messageLbl.setBackground(Color.YELLOW);
                        timeLbl.setBackground(Color.YELLOW);

                    } else {
                        messageLbl.setBounds(5, 5 + 45 * i, messageLn, 30);
                        timeLbl.setBounds(5, 5 + 30 + 45 * i, 35, 10);
                        messageLbl.setHorizontalAlignment(SwingConstants.LEFT);
                        messageLbl.setBackground(Color.WHITE);
                        timeLbl.setBackground(Color.WHITE);
                    }
                    chatPnl.add(messageLbl);
                    chatPnl.add(timeLbl);
                }
            } else {
                ArrayList<Group.MessageItem> chat = me.groups.get(contactId).onlineChat;
                int i = -1;
                for (Group.MessageItem msg : chat) {
                    i++;
                    String message = msg.message.message.toString();
                    int messageLn = message.length() * 12;
                    String senderName;
                    if (msg.senderId.equals(me.Id)) {
                        senderName = "me";
                    } else {
                        Contact con = me.contacts.get(msg.senderId.trim());
                        if (con != null) {
                            senderName = con.name.trim();
                        } else {
                            senderName = msg.senderId.trim();
                        }

                    }
                    int senderNameLn = senderName.length() * 12;
                    String time = msg.sendingTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    JLabel senderLbl = new JLabel();
                    senderLbl.setFont(new Font(senderLbl.getFont().getName(), Font.PLAIN, 12));
                    JLabel messageLbl = new JLabel();
                    messageLbl.setFont(new Font(messageLbl.getFont().getName(), Font.PLAIN, 18));
                    JLabel timeLbl = new JLabel();
                    timeLbl.setFont(new Font(timeLbl.getFont().getName(), Font.PLAIN, 10));
                    senderLbl.setOpaque(true);
                    messageLbl.setOpaque(true);
                    timeLbl.setOpaque(true);
                    senderLbl.setText(senderName);
                    messageLbl.setText(message);
                    timeLbl.setText(time);
                    if (msg.senderId.equals(Id)) {
                        senderLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 55 * i, senderNameLn, 10);
                        messageLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 10 + 55 * i, messageLn, 30);
                        timeLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 10 + 30 + 55 * i, 35, 10);
                        messageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                        senderLbl.setBackground(Color.YELLOW);
                        messageLbl.setBackground(Color.YELLOW);
                        timeLbl.setBackground(Color.YELLOW);

                    } else {
                        senderLbl.setBounds(5, 5 + 55 * i, senderNameLn, 10);
                        messageLbl.setBounds(5, 5 + 10 + 55 * i, messageLn, 30);
                        timeLbl.setBounds(5, 5 + 10 + 30 + 55 * i, 35, 10);
                        messageLbl.setHorizontalAlignment(SwingConstants.LEFT);
                        senderLbl.setBackground(Color.WHITE);
                        messageLbl.setBackground(Color.WHITE);
                        timeLbl.setBackground(Color.WHITE);
                    }
                    chatPnl.add(senderLbl);
                    chatPnl.add(messageLbl);
                    chatPnl.add(timeLbl);
                }
            }

            if (specialUser.contains(contactId)) {
                specialUser.remove(contactId);
            }
        }
        this.repaint();

    }
    String recieverId = "";

    private void chatBtnActionPerformed(java.awt.event.ActionEvent evt) {
        chatPnl.removeAll();
        sendBtn.setEnabled(true);
        JButton clickedBtn = (JButton) evt.getSource();
        clickedBtn.setFont(new Font("sansserif", 0, 20));
        String contactId = clickedBtn.getName().trim();
        recieverId = contactId;
        if (recieverId.contains("wa")) {
            ArrayList<Contact.MessageItem> chat = me.contacts.get(contactId).onlineChat;

            int i = -1;
            for (Contact.MessageItem msg : chat) {
                i++;
                String message = msg.message.message.toString();
                int messageLn = message.length() * 12;
                String time = msg.sendingTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                JLabel messageLbl = new JLabel();
                messageLbl.setFont(new Font(messageLbl.getFont().getName(), Font.PLAIN, 18));
                JLabel timeLbl = new JLabel();
                timeLbl.setFont(new Font(timeLbl.getFont().getName(), Font.PLAIN, 10));
                messageLbl.setOpaque(true);
                timeLbl.setOpaque(true);
                messageLbl.setText(message);
                timeLbl.setText(time);
                if (msg.senderId.equals(Id)) {
                    messageLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 45 * i, messageLn, 30);
                    timeLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 30 + 45 * i, 35, 10);
                    messageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    messageLbl.setBackground(Color.YELLOW);
                    timeLbl.setBackground(Color.YELLOW);

                } else {
                    messageLbl.setBounds(5, 5 + 45 * i, messageLn, 30);
                    timeLbl.setBounds(5, 5 + 30 + 45 * i, 35, 10);
                    messageLbl.setHorizontalAlignment(SwingConstants.LEFT);
                    messageLbl.setBackground(Color.WHITE);
                    timeLbl.setBackground(Color.WHITE);
                }
                chatPnl.add(messageLbl);
                chatPnl.add(timeLbl);
            }
            this.repaint();

            if (specialUser.contains(contactId)) {
                specialUser.remove(contactId);
            }
            clickedBtn.setText(me.contacts.get(contactId).name);
        } else {
            ArrayList<Group.MessageItem> chat = me.groups.get(contactId).onlineChat;

            int i = -1;
            for (Group.MessageItem msg : chat) {
                i++;
                String senderName;
                if (msg.senderId.equals(me.Id)) {
                    senderName = "me";
                } else {
                    Contact con = me.contacts.get(msg.senderId.trim());
                    if (con != null) {
                        senderName = con.name.trim();
                    } else {
                        senderName = msg.senderId.trim();
                    }
                    if (con != null) {
                        senderName = con.name.trim();
                    } else {
                        senderName = msg.senderId.trim();
                    }
                }
                int senderNameLn = senderName.length() * 12;
                String message = msg.message.message.toString();
                int messageLn = message.length() * 12;
                String time = msg.sendingTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                JLabel senderLbl = new JLabel();
                senderLbl.setFont(new Font(senderLbl.getFont().getName(), Font.PLAIN, 12));
                JLabel messageLbl = new JLabel();
                messageLbl.setFont(new Font(messageLbl.getFont().getName(), Font.PLAIN, 18));
                JLabel timeLbl = new JLabel();
                timeLbl.setFont(new Font(timeLbl.getFont().getName(), Font.PLAIN, 10));
                senderLbl.setOpaque(true);
                messageLbl.setOpaque(true);
                timeLbl.setOpaque(true);
                senderLbl.setText(senderName);
                messageLbl.setText(message);
                timeLbl.setText(time);
                if (msg.senderId.equals(Id)) {
                    senderLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 55 * i, senderNameLn, 10);
                    messageLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 10 + 55 * i, messageLn, 30);
                    timeLbl.setBounds(chatPnl.getWidth() - messageLn - 5, 5 + 10 + 30 + 55 * i, 35, 10);
                    messageLbl.setHorizontalAlignment(SwingConstants.RIGHT);
                    senderLbl.setBackground(Color.YELLOW);
                    messageLbl.setBackground(Color.YELLOW);
                    timeLbl.setBackground(Color.YELLOW);

                } else {
                    senderLbl.setBounds(5, 5 + 55 * i, senderNameLn, 10);
                    messageLbl.setBounds(5, 5 + 10 + 55 * i, messageLn, 30);
                    timeLbl.setBounds(5, 5 + 10 + 30 + 55 * i, 35, 10);
                    messageLbl.setHorizontalAlignment(SwingConstants.LEFT);
                    senderLbl.setBackground(Color.WHITE);
                    messageLbl.setBackground(Color.WHITE);
                    timeLbl.setBackground(Color.WHITE);
                }
                chatPnl.add(senderLbl);
                chatPnl.add(messageLbl);
                chatPnl.add(timeLbl);
            }
            this.repaint();

            if (specialUser.contains(contactId)) {
                specialUser.remove(contactId);
            }
            clickedBtn.setText(me.groups.get(contactId).groupName);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myIdLbl = new javax.swing.JLabel();
        myUserNameLbl = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        contactIdTB = new javax.swing.JTextField();
        addContactBtn = new javax.swing.JButton();
        msgTB = new javax.swing.JTextField();
        sendBtn = new javax.swing.JButton();
        chatPnl = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        addGroupBtn = new javax.swing.JMenu();
        bCastBtn = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("WhasApp");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        myIdLbl.setFont(new java.awt.Font("sansserif", 2, 18)); // NOI18N
        myIdLbl.setText("myID");

        myUserNameLbl.setFont(new java.awt.Font("sansserif", 2, 18)); // NOI18N
        myUserNameLbl.setText("myUserName");

        jLabel1.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel1.setText("ID");

        jLabel2.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        jLabel2.setText("username");

        contactIdTB.setFont(new java.awt.Font("SansSerif", 0, 24)); // NOI18N

        addContactBtn.setFont(new java.awt.Font("Agency FB", 1, 18)); // NOI18N
        addContactBtn.setText("add Contact");
        addContactBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContactBtnActionPerformed(evt);
            }
        });

        msgTB.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        msgTB.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 0, 0)));

        sendBtn.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        sendBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mywhatsapp/computer-icons-button-send-email-button.jpg"))); // NOI18N
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendBtnActionPerformed(evt);
            }
        });

        chatPnl.setBackground(new java.awt.Color(241, 237, 237));
        chatPnl.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout chatPnlLayout = new javax.swing.GroupLayout(chatPnl);
        chatPnl.setLayout(chatPnlLayout);
        chatPnlLayout.setHorizontalGroup(
            chatPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );
        chatPnlLayout.setVerticalGroup(
            chatPnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 658, Short.MAX_VALUE)
        );

        jMenuBar1.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N

        addGroupBtn.setText("add Group");
        addGroupBtn.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        addGroupBtn.setPreferredSize(new java.awt.Dimension(100, 33));
        addGroupBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addGroupBtnMouseClicked(evt);
            }
        });
        jMenuBar1.add(addGroupBtn);

        bCastBtn.setText("broadCast");
        bCastBtn.setFont(new java.awt.Font("Agency FB", 1, 24)); // NOI18N
        bCastBtn.setPreferredSize(new java.awt.Dimension(100, 33));
        bCastBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bCastBtnMouseClicked(evt);
            }
        });
        jMenuBar1.add(bCastBtn);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(contactIdTB, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(addContactBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(166, 166, 166)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(myIdLbl))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(jLabel2)
                        .addGap(39, 39, 39)
                        .addComponent(myUserNameLbl))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(chatPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(msgTB, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addContactBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(663, 663, 663))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(contactIdTB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(myIdLbl)
                                    .addComponent(jLabel1))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(myUserNameLbl)
                                    .addComponent(jLabel2))))
                        .addGap(18, 18, 18)
                        .addComponent(chatPnl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(msgTB)
                    .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            serverRef.disconnect(Id);

        } catch (RemoteException ex) {
            Logger.getLogger(ClientGui.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void addContactBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addContactBtnActionPerformed
        String contactId = contactIdTB.getText().trim();
        try {
            User user = serverRef.AddContact(Id, contactId);
            if (user == null) {
                contactIdTB.setText("Impossible!");
            } else {
                me = user;
                Refresh();
                contactIdTB.setText("");

            }
        } catch (RemoteException ex) {
            Logger.getLogger(ClientGui.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_addContactBtnActionPerformed

    private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendBtnActionPerformed
        try {
            if (recieverId.contains("wa")) {
                me = serverRef.SendMessage(Id, recieverId, IMyWhatsappServer.MessageType.Text, msgTB.getText());
            } else {
                System.out.println(recieverId);
                me = serverRef.SendGroupMessage(recieverId.trim(), Id, me.groups.get(recieverId).groupMembersId, IMyWhatsappServer.MessageType.Text, msgTB.getText());
            }
            Refresh();
            msgTB.setText("");

        } catch (RemoteException ex) {
            Logger.getLogger(ClientGui.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_sendBtnActionPerformed

    private void addGroupBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addGroupBtnMouseClicked
        GroupForm gF = new GroupForm(me, serverRef);
        gF.bCastMsgTB.setVisible(false);
        gF.groupNameTB.setVisible(true);
        gF.setVisible(true);

     }//GEN-LAST:event_addGroupBtnMouseClicked

    private void bCastBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bCastBtnMouseClicked
        GroupForm gF = new GroupForm(me, serverRef);
        gF.bCastMsgTB.setVisible(true);
        gF.groupNameTB.setVisible(false);
        gF.setVisible(true);
    }//GEN-LAST:event_bCastBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGui.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new ClientGui("").setVisible(true);

                } catch (NotBoundException ex) {
                    Logger.getLogger(ClientGui.class
                            .getName()).log(Level.SEVERE, null, ex);

                } catch (MalformedURLException ex) {
                    Logger.getLogger(ClientGui.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addContactBtn;
    private javax.swing.JMenu addGroupBtn;
    private javax.swing.JMenu bCastBtn;
    private javax.swing.JPanel chatPnl;
    private javax.swing.JTextField contactIdTB;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JTextField msgTB;
    private javax.swing.JLabel myIdLbl;
    private javax.swing.JLabel myUserNameLbl;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
