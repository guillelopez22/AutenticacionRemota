/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author guill
 */
public class Server1 {
     
    private void iniciarServidor(){
        try {
            // Crear el repositorio en el puerto 1099
            Registry registry = LocateRegistry.createRegistry(25000);
             
            // Crea un nuevo servicio y lo registra en el repositorio
            registry.rebind("miMensaje", new clientImpl());
        } catch (Exception e) {
            e.printStackTrace();
        }      
        System.out.println("El sistema esta listo");
    }

    public static void main(String[] args) {
        Server1 servidor = new Server1();
        servidor.iniciarServidor();
    }

}   
