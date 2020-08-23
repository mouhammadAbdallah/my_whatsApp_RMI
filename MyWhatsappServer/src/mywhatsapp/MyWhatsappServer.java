/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDateTime;
import mywhatsapp.User.Contact;

/**
 *
 * @author Moham
 */
public class MyWhatsappServer extends UnicastRemoteObject implements IMyWhatsappServer {

    static HashMap<String, IClient> connectedUser;
    static HashMap<String, User> clientsUser;

    static int newId = 0;

    public MyWhatsappServer() throws RemoteException {
        connectedUser = new HashMap();
        clientsUser = new HashMap();
    }

    @Override
    public User connect(String id, IClient client) throws RemoteException {
        connectedUser.put(id, client);
        System.out.println("[+] the client of Id: " + id + " is now connected ");
        User user = clientsUser.get(id);
        return user;
    }

    @Override
    public void ReadOffLineMessageOfAllContacts(String id) throws RemoteException {
        clientsUser.get(id).ReadOffLineMessageOfAllContactsAndGroups();
        System.out.println("[*] the client of Id: " + id + " reads his offline messages ");
    }

    @Override
    public void disconnect(String id) throws RemoteException {
        connectedUser.remove(id);
        System.out.println("[*] the client of Id: " + id + " is now disconnected ");
    }
/////////////////////////////////////////////////////////fini esta3mel connected List kermal shouf min online

    public static void main(String[] args) {

        try {
            System.out.println("[*] Creating Registry, port: 2000 ...");
            LocateRegistry.createRegistry(2000);
            System.out.println("[+] Registry is successfully created ");

            System.out.println();

            System.out.println("[*] Running MyWhatsappServer on rmi://127.0.0.1:2000/mywhatsappserver");
            IMyWhatsappServer server = new MyWhatsappServer();
            System.out.println("[+] MyWhatsappServer is successfully turned On ");
            System.out.println(".... ");
            Naming.rebind("rmi://127.0.0.1:2000/mywhatsappserver", server);
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(MyWhatsappServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public String CreateClient(String name, String pass) throws RemoteException {
        newId++;
        User user = new User("wa" + newId, name, pass);
        clientsUser.put("wa" + newId, user);
        System.out.println("[+] new client with id: wa" + newId + " and name: " + name + " and password: " + pass);
        return "wa" + newId;
    }

    @Override
    public String IdIfValidPass(String id, String pass) throws RemoteException {
        if (!clientsUser.containsKey(id)) {
            System.out.println("[-] a client with invalid id: " + id + " tried to sign in");
            return "0";
        } else {
            String val = clientsUser.get(id).pass;
            val = val.trim();
            if (val.equals(pass)) {
                System.out.println("[+] the client of id: " + id + " and password: " + pass + " successfully sign in");
                return id;
            } else {
                System.out.println("[-] the client of id: " + id + " tried to sign in with an invalid password: " + pass);
                return "0";
            }
        }

    }

    @Override
    public User AddContact(String myId, String contactId) throws RemoteException {
        if (!clientsUser.containsKey(contactId)) {
            System.out.println("[-] the client of Id: " + myId + " failed to add the contact: " + contactId + " because it is invalid");
            return null;
        }
        if (clientsUser.get(myId).contacts.containsKey(contactId)) {
            System.out.println("[-] the client of Id: " + myId + " failed to add the contact: " + contactId + " because it is already in his contacts");
            return null;
        }
        clientsUser.get(myId).contacts.put(contactId, new Contact(contactId, clientsUser.get(contactId).name));
        clientsUser.get(contactId).contacts.put(myId, new Contact(myId, clientsUser.get(myId).name));
        System.out.println("[+] the client of Id: " + myId + " add the contact: " + contactId);
        return SendMessage(myId, contactId, MessageType.Text, "I added you to my contacts");
    }

    @Override
    public User AddGroup(String groupName, String[] memberIds) throws RemoteException {
        newId++;
        for (String memberId : memberIds) {
            clientsUser.get(memberId).groups.put("gp" + newId, new User.Group("gp" + newId, groupName, memberIds));
        }
        System.out.println("[+] new group is created with the member Ids: " + memberIds.toString());
        return SendGroupMessage("gp" + newId, memberIds[0], memberIds, MessageType.Text, "I added you to this Group");
    }

    @Override
    public User SendGroupMessage(String groupId, String senderId, String[] memberIds, MessageType messageType, Object message) throws RemoteException {
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String memberId : memberIds) {
            IClient client = connectedUser.get(memberId);
            if (client != null) {
                clientsUser.get(memberId).groups.get(groupId).AddOnlineMessage(senderId, groupId, messageType, message, localDateTime);
                client.Notify(clientsUser.get(memberId), groupId);
                System.out.println("[*] the client: " + senderId + " send online to group: " + groupId + " at " + localDateTime);
            } else {
                clientsUser.get(memberId).groups.get(groupId).AddOfflineMessage(senderId, groupId, messageType, message, localDateTime);
                System.out.println("[*] the client: " + senderId + " send offline to group: " + groupId + " at " + localDateTime);

            }

        }

        return clientsUser.get(senderId.trim());
    }

    @Override
    public User SendBroadCast(String senderId, String[] recieversIds, MessageType messageType, Object message) throws RemoteException {
        LocalDateTime localDateTime = LocalDateTime.now();
        for (String recieverId : recieversIds) {
            if (!recieverId.equals(senderId)) {
                clientsUser.get(senderId).contacts.get(recieverId).AddOnlineMessage(senderId, recieverId, messageType, message, localDateTime);
                IClient client = connectedUser.get(recieverId);
                if (client != null) {
                    clientsUser.get(recieverId).contacts.get(senderId).AddOnlineMessage(senderId, recieverId, messageType, message, localDateTime);
                    client.Notify(clientsUser.get(recieverId), senderId);
                    System.out.println("[*] the client: " + senderId + " send online a broadcast to: " + recieverId + " at " + localDateTime);
                } else {
                    clientsUser.get(recieverId).contacts.get(senderId).AddOfflineMessage(senderId, recieverId, messageType, message, localDateTime);
                    System.out.println("[*] the client: " + senderId + " send offline a broadcast to: " + recieverId + " at " + localDateTime);

                }
            }

        }
        connectedUser.get(senderId).Notify(clientsUser.get(senderId), "0");
        return clientsUser.get(senderId.trim());
    }

    @Override
    public User SendMessage(String senderId, String recieverId, MessageType messageType, Object message) throws RemoteException {
        LocalDateTime localDateTime = LocalDateTime.now();
        clientsUser.get(senderId).contacts.get(recieverId).AddOnlineMessage(senderId, recieverId, messageType, message, localDateTime);
        IClient client = connectedUser.get(recieverId);
        if (client != null) {
            clientsUser.get(recieverId).contacts.get(senderId).AddOnlineMessage(senderId, recieverId, messageType, message, localDateTime);
            client.Notify(clientsUser.get(recieverId), senderId);
            System.out.println("[*] the client: " + senderId + " send online to: " + recieverId + " at " + localDateTime);

        } else {
            clientsUser.get(recieverId).contacts.get(senderId).AddOfflineMessage(senderId, recieverId, messageType, message, localDateTime);
            System.out.println("[*] the client: " + senderId + " send offline to: " + recieverId + " at " + localDateTime);
        }
        return clientsUser.get(senderId);
    }

}
