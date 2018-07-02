/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author guill
 */
public class coordService extends UnicastRemoteObject implements clientMethods{
    private ArrayList<clientMethods> servers = new ArrayList<>();
    public coordService () throws RemoteException, NotBoundException{
            
            Registry Registry = LocateRegistry.getRegistry(25000);
            Registry Registry2 = LocateRegistry.getRegistry(25001);
            clientMethods SERVER1 = (clientMethods) Registry.lookup("SERVER1");
            clientMethods SERVER2 = (clientMethods) Registry2.lookup("SERVER2");
            servers.add(SERVER1);
            servers.add(SERVER2);
    }

    @Override
    public UUID auth(Usuario usuario) throws RemoteException {
        Random r = new Random();
        
        clientMethods chosen = servers.get(r.nextInt(1));
        chosen.auth(usuario);
        return usuario.getUuid();
    }

    @Override
    public void registrar(Usuario usuario) throws RemoteException {
        Random r = new Random();
        
        clientMethods chosen = servers.get(r.nextInt(1));
        chosen.registrar(usuario);
    }

    @Override
    public Usuario lookUpUserUUID(UUID uuid) throws RemoteException {
        Random r = new Random();
        
        clientMethods chosen = servers.get(r.nextInt(1));
        chosen.lookUpUserUUID(uuid);
        return null;
    }

    @Override
    public Usuario lookUpUserByUSERNAME(String username) throws RemoteException {
        Random r = new Random();
        Usuario temporal = new Usuario();
        clientMethods chosen = servers.get(r.nextInt(1));
        temporal =chosen.lookUpUserByUSERNAME(username);
        return temporal;
    }

    @Override
    public boolean deleteUser(Usuario usuario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUserName(String old_username, String new_username) throws RemoteException {
        Random r = new Random();
        
        clientMethods chosen = servers.get(r.nextInt(1));
        chosen.updateUserName(old_username, new_username);
    }

    @Override
    public ArrayList<Usuario> getUsers() throws RemoteException {
        Random r = new Random();
        ArrayList<Usuario> temps = new ArrayList<>();
        clientMethods chosen = servers.get(r.nextInt(1));
        temps = chosen.getUsers();
        
        return temps;
        
    }
    
}
