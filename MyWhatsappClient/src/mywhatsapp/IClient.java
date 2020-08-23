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
public interface IClient extends Remote{
    public void Notify(User user,String senderId) throws RemoteException;
    
}
