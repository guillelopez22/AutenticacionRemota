/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autenticacion.remota;

/**
 *
 * @author Guillermo
 */
import static autenticacion.remota.Login.encrypt;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JOptionPane;
import org.bson.Document;

public class Client {

    private static Socket socket;
    private static Queue<String> credentials = new LinkedList<>();
    private static MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
    private static MongoClient client = new MongoClient(uri);
    private static MongoDatabase db = client.getDatabase(uri.getDatabase());
    private static MongoCollection<Document> users = db.getCollection("users");

    public static void main(String args[]) {
        try {
            String UUID = "ERR";
            String username = "", password = "";
            String host = "localhost";
            int port = 25000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            credentials.add(message);
            System.out.println("Message received from the server : " + message);

            //Send the message to the server
            System.out.println("Processing Authentication....");
            String[] cred = credentials.poll().split(",");
            username = cred[0];
            password = cred[1];
            BasicDBObject andQuery = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
            obj.add(new BasicDBObject("username", username));
            obj.add(new BasicDBObject("password", password));
            andQuery.put("$and", obj);
            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            MongoCursor<Document> cursor = users.find(andQuery).iterator();
            String sendMessage = "";
            Document doc = null;
            while (cursor.hasNext()) {
                doc = cursor.next();
            }
            if (doc == null) {
                sendMessage = "Error en la credenciales, porfavor verifique de nuevo";
            } else if (doc.get("username").equals(username) && doc.get("password").equals(password)) {
                sendMessage = doc.get("UUID").toString();
            }
            
            bw.write(sendMessage);
            bw.flush();
            System.out.println("Message sent to the server : " + sendMessage);

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            //Closing the socket
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
