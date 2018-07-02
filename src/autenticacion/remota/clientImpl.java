/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author guill
 */
public class clientImpl extends UnicastRemoteObject implements clientMethods {

    MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
    MongoClient client = new MongoClient(uri);
    MongoDatabase db = client.getDatabase(uri.getDatabase());
    MongoCollection<Document> users = db.getCollection("users");

    @Override
    public String auth(Usuario usuario) throws RemoteException {

        String message = "";
        Document doc = users.find(and(eq("username", usuario.getUsername()), (eq("password", usuario.getPassword())))).first();
        if (doc.get("username").equals(usuario.getUsername()) && doc.get("password").equals(usuario.getPassword())) {
            message = doc.get("UUID").toString();
        }

        return message;
    }

    @Override
    public void registrar(Usuario usuario) throws RemoteException {
        String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Document document = new Document("UUID", usuario.getUuid())
                .append("nombre", usuario.getPrimer_nombre() + " " + usuario.getSegundo_nombre())
                .append("username", usuario.getUsername())
                .append("password", usuario.getPassword())
                .append("fecha_creacion", date.toString())
                .append("ultimo acceso", date.toString());
        users.insertOne(document);
    }

    @Override
    public Usuario lookUpUserUUID(UUID uuid) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Usuario lookUpUserByUSERNAME(String username) throws RemoteException {
        Document doc = users.find(eq("username", username)).first();
        String[] nombres = doc.get("nombre").toString().split(" ");
        Usuario usuario = new Usuario((UUID)doc.get("UUID"), nombres[0], nombres[1], doc.get("username").toString(), doc.get("password").toString(), doc.get("fecha_creacion").toString());
        return usuario;
    }

    @Override
    public boolean deleteUser(Usuario usuario) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUserName(String old_username, String new_username) throws RemoteException {
        Bson filter = new Document("username", old_username);
        Bson newValue = new Document("username", new_username);
        Bson updateOperationDocument = new Document("$set", newValue);
        users.updateOne(filter, updateOperationDocument);
    }

    @Override
    public ArrayList<Usuario> getUsers() throws RemoteException {
        ArrayList<Usuario> all_users = new ArrayList<>();
        MongoCursor<Document> cursor = users.find().iterator();
        Document doc;
        String nombre, username, fecha_c, fecha_a;
        while(cursor.hasNext()){
            doc = cursor.next();
            nombre = (String) doc.get("nombre");
            String[] nombres = nombre.split(" ");
            System.out.println(nombre);
            username = (String) doc.get("username");
            System.out.println(username);
            fecha_c = (String) doc.get("fecha_creacion");
            System.out.println(fecha_c);
            fecha_a = (String) doc.get("ultimo acceso");
            System.out.println(fecha_a);
            all_users.add(new Usuario(nombres[0],nombres[1], username, fecha_a));
        }
        return all_users;
    }
    
}
