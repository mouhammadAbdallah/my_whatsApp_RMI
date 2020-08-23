/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import mywhatsapp.IMyWhatsappServer.MessageType;

/**
 *
 * @author Moham
 */
public class User implements Serializable {

    public String Id, name, pass;
    public HashMap<String, Contact> contacts;
    public HashMap<String, Group> groups;

    public User(String Id, String name, String pass) {
        this.Id = Id;
        this.name = name;
        this.pass = pass;
        this.contacts = new HashMap();
        this.groups=new HashMap();

    }

    public void ReadOffLineMessageOfAllContactsAndGroups() {
        for (Contact con : contacts.values()) {
            con.ReadOffLineMessageOfThisContact();
        }
        for (Group group : groups.values()) {
            group.ReadOffLineMessageOfThisContact();
        }

    }

    public static class Contact implements Serializable {

        public String Id;
        public String name;
        public ArrayList<MessageItem> onlineChat;
        public ArrayList<MessageItem> offlineChat;

        public Contact(String Id, String name) {
            this.Id = Id;
            this.name = name;
            this.onlineChat = new ArrayList();
            this.offlineChat = new ArrayList();
        }

        public void AddOnlineMessage(String senderId, String recieverId, MessageType messageType, Object message, LocalDateTime sendingTime) {
            onlineChat.add(new MessageItem(senderId, recieverId, new Message(messageType, message), sendingTime));
        }

        public void AddOfflineMessage(String senderId, String recieverId, MessageType messageType, Object message, LocalDateTime sendingTime) {
            offlineChat.add(new MessageItem(senderId, recieverId, new Message(messageType, message), sendingTime));
        }

        public void ReadOffLineMessageOfThisContact() {
            for (MessageItem mI : offlineChat) {
                AddOnlineMessage(mI.senderId, mI.recieverId, mI.message.messageType, mI.message.message, mI.sendingTime);
            }
            offlineChat = new ArrayList();
        }

        public class MessageItem implements Serializable {

            public String senderId;
            public String recieverId;
            public Message message;
            public LocalDateTime sendingTime;

            public MessageItem(String senderId, String recievedId, Message message, LocalDateTime sendingTime) {
                this.senderId = senderId;
                this.recieverId = recievedId;
                this.message = new Message(message.messageType, message.message);
                this.sendingTime = sendingTime;
            }
        }

        public class Message implements Serializable {

            public MessageType messageType;
            public Object message;

            public Message(MessageType messageType, Object message) {
                this.messageType = messageType;
                this.message = message;
            }

        }

    }

    public static class Group implements Serializable {

        public String groupId;
        public String groupName;
        public String[] groupMembersId;
        public ArrayList<MessageItem> onlineChat;
        public ArrayList<MessageItem> offlineChat;

        public Group(String Id, String name,String[]groupMembersId) {
            this.groupId = Id;
            this.groupName = name;
            this.groupMembersId=groupMembersId;
            this.onlineChat = new ArrayList();
            this.offlineChat = new ArrayList();
        }

        public void AddOnlineMessage(String senderId, String recieverId, MessageType messageType, Object message, LocalDateTime sendingTime) {
            onlineChat.add(new MessageItem(senderId, recieverId, new Message(messageType, message), sendingTime));
        }

        public void AddOfflineMessage(String senderId, String recieverId, MessageType messageType, Object message, LocalDateTime sendingTime) {
            offlineChat.add(new MessageItem(senderId, recieverId, new Message(messageType, message), sendingTime));
        }

        public void ReadOffLineMessageOfThisContact() {
            for (MessageItem mI : offlineChat) {
                AddOnlineMessage(mI.senderId, mI.recieverId, mI.message.messageType, mI.message.message, mI.sendingTime);
            }
            offlineChat = new ArrayList();
        }

        public class MessageItem implements Serializable {

            public String senderId;
            public String recieverId;
            public Message message;
            public LocalDateTime sendingTime;

            public MessageItem(String senderId, String recievedId, Message message, LocalDateTime sendingTime) {
                this.senderId = senderId;
                this.recieverId = recievedId;
                this.message = new Message(message.messageType, message.message);
                this.sendingTime = sendingTime;
            }
        }

        public class Message implements Serializable {

            public MessageType messageType;
            public Object message;

            public Message(MessageType messageType, Object message) {
                this.messageType = messageType;
                this.message = message;
            }

        }

    }
}
