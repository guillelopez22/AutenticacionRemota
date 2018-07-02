/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

/**
 *
 * @author Guillermo
 */
public class Usuario implements Serializable{
    private UUID uuid;
    private String primer_nombre;
    private String segundo_nombre;
    private String username;
    private String password;
    private String fecha_creacion;
    private ArrayList<String> fechas_acceso_servidor;

    public Usuario() {
    }

    public Usuario(String primer_nombre, String segundo_nombre, String username, String fecha_creacion) {
        this.primer_nombre = primer_nombre;
        this.segundo_nombre = segundo_nombre;
        this.username = username;
        this.fecha_creacion = fecha_creacion;
    }



    public Usuario(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public Usuario(UUID uuid, String primer_nombre, String segundo_nombre, String username, String password, String fecha_creacion) {
        this.uuid = uuid;
        this.primer_nombre = primer_nombre;
        this.segundo_nombre = segundo_nombre;
        this.username = username;
        this.password = encrypt(password);
        this.fecha_creacion = fecha_creacion;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPrimer_nombre() {
        return primer_nombre;
    }

    public void setPrimer_nombre(String primer_nombre) {
        this.primer_nombre = primer_nombre;
    }

    public String getSegundo_nombre() {
        return segundo_nombre;
    }

    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = encrypt(password);
    }

    public String getFecha_creacion() {
        return fecha_creacion;
    }

    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    public ArrayList<String> getFechas_acceso_servidor() {
        return fechas_acceso_servidor;
    }

    public void setFechas_acceso_servidor(String fecha_acceso_servidor) {
        this.fechas_acceso_servidor.add(fecha_acceso_servidor);
    }

    @Override
    public String toString() {
        return uuid +" "+ primer_nombre +" "+ segundo_nombre +" "+ username +" "+ fecha_creacion;
    }
    public static String encrypt(String pass) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = pass.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return null;

    }
}
