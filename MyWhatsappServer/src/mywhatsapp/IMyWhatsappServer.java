/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Salah
 */
public interface IMyWhatsappServer extends Remote {

    public User connect(String id, IClient client) throws RemoteException;

    public void ReadOffLineMessageOfAllContacts(String id) throws RemoteException;

    public void disconnect(String id) throws RemoteException;

    public String CreateClient(String name, String pass) throws RemoteException;

    public String IdIfValidPass(String id, String pass) throws RemoteException;

    public User AddContact(String myId, String contactId) throws RemoteException;

    public enum MessageType {
        Text, Image, Video, Voice
    }

    public User SendMessage(String senderId, String recieverId, MessageType messageType, Object message) throws RemoteException;

    public User AddGroup(String groupName, String[] memberIds) throws RemoteException;

    public User SendGroupMessage(String groupId, String senderId, String[] memberIds, MessageType messageType, Object message) throws RemoteException;

    public User SendBroadCast(String senderId, String[] recieversIds, MessageType messageType, Object message) throws RemoteException;
}
