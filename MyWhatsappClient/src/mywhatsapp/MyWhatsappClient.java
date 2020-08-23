/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mywhatsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import java.awt.EventQueue;
import javax.swing.SwingUtilities;

/**
 *
 * @author Moham
 */
public class MyWhatsappClient extends UnicastRemoteObject implements IClient {

    /**
     * @param args the command line arguments
     */
    public static String RMIRef = "rmi://127.0.0.1:2000/mywhatsappserver";

    JTextArea area;
    static SignInGui form;

    public MyWhatsappClient() throws RemoteException {

    }

    @Override
    public void Notify(User user, String senderId) throws RemoteException {
        form.whatsapp.me = user;
        if (!senderId.equals("0")) {
            form.whatsapp.specialUser.add(senderId);
        }
        form.whatsapp.Refresh();
    }

    public static void main(String[] args) throws NotBoundException {

        form = new SignInGui();
    }

}
