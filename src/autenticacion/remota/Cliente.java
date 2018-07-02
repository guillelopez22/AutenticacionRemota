/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author guill
 */
public class Cliente {
    public clientMethods coordinator = null;
    
    
    public void run() throws RemoteException, NotBoundException, UnknownHostException{
        
        Registry service = LocateRegistry.getRegistry("172.16.17.8",25005);
        coordinator  = (clientMethods) service.lookup("COORDINATOR");
    }  
}
