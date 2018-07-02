/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author guill
 */
public interface clientMethods extends Remote{
    String auth(Usuario usuario) throws RemoteException;
    void registrar(Usuario usuario) throws RemoteException;
    Usuario lookUpUserUUID (UUID uuid) throws RemoteException;
    Usuario lookUpUserByUSERNAME (String username) throws RemoteException;
    boolean deleteUser(Usuario usuario) throws RemoteException;
    void updateUserName (String old_username, String new_username) throws RemoteException;
    ArrayList<Usuario> getUsers () throws RemoteException;
}
