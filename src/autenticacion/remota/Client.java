
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
import org.bson.Document;

public class Client {

    private static Queue<String> credentials = new LinkedList<>();
    private static Socket socket;
    private static MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
    private static MongoClient client = new MongoClient(uri);
    private static MongoDatabase db = client.getDatabase(uri.getDatabase());
    private static MongoCollection<Document> users = db.getCollection("users");

    public static void main(String args[]) {
        try {
            String host = "localhost";
            int port = 25000;
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            String username = "memoln", password = "202cb962ac5975b964b7152d234b70";
            credentials.add(username+","+password);
            //Send the message to the server
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
            bw.newLine();
            bw.flush();
            if (bw != null) {
                System.out.println("si hay");
            } else {
                System.out.println("no hay nada");
            }
            System.out.println("Message sent to the server : " + sendMessage);

            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String message = br.readLine();
            System.out.println("Message received from the server : " + message);
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
