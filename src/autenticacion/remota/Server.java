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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.bson.Document;

public class Server {
private static Queue<String> credentials = new LinkedList<>();
    private static MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
    private static MongoClient client = new MongoClient(uri);
    private static MongoDatabase db = client.getDatabase(uri.getDatabase());
    private static MongoCollection<Document> users = db.getCollection("users");
    private static Socket socket;

    public static void main(String[] args) {
        try {

            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");

            //Server is running always. This is done using this while(true) loop
            while (true) {
                //Reading the message from the client
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                credentials.add(message);
                System.out.println("Message received from client is " + message);

                //Multiplying the number by 2 and forming the return message
                String returnMessage="";

                System.out.println("Processing Authentication....");
                String[] cred = credentials.poll().split(",");
                String username = cred[0];
                String password = cred[1];
                BasicDBObject andQuery = new BasicDBObject();
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("username", username));
                obj.add(new BasicDBObject("password", password));
                andQuery.put("$and", obj);
                MongoCursor<Document> cursor = users.find(andQuery).iterator();
                Document doc = cursor.next();
                while (cursor.hasNext()) {
                    doc = cursor.next();
                }
                if (doc == null) {
                    returnMessage = "Error en la credenciales, porfavor verifique de nuevo";
                } else if (doc.get("username").equals(username) && doc.get("password").equals(password)) {
                    returnMessage = doc.get("UUID").toString();
                }

                //Sending the response back to the client.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(returnMessage);
                System.out.println("Message sent to the client is " + returnMessage);
                bw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
            }
        }
    }
}
