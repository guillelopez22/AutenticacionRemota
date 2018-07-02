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
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.bson.Document;

public class Auth_Server2 {

    private static Socket socket;
    private static final Queue<String> credentials = new LinkedList<>();

    private static String username = "", password = "", message;

    public static void main(String[] args) {
        try {
            int port = 25001;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25001");

            //Server is running always. This is done using this while(true) loop
            while (true) {
                //Reading the message from the client
                socket = serverSocket.accept();

                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                message = br.readLine();
                System.out.println("received message: " + message);
                credentials.add(message);
                //Send the message to the server
                System.out.println("Processing Authentication....");
                String[] cred = credentials.poll().split(",");
                username = cred[0];
                password = cred[1];
                String sendMessage = auth(message);
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(sendMessage);
                bw.newLine();
                bw.flush();
                if (bw != null) {
                    System.out.println("si hay");
                } else {
                    System.out.println("no hay nada");
                }
                System.out.println("Message sent to the server : " + sendMessage);

            }
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static String auth(String message) throws SocketException {
        MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());
        MongoCollection<Document> users = db.getCollection("users");
        Document doc = users.find(and(eq("username", username), (eq("password", password)))).first();
        if (doc.get("username").equals(username) && doc.get("password").equals(password)) {
            message = doc.get("UUID").toString();
        }
        
        return message;

    }
}
//public static void main(String[] args)
//    {
//        try
//        {
// 
//            int port = 25000;
//            ServerSocket serverSocket = new ServerSocket(port);
//            System.out.println("Server Started and listening to the port 25000");
// 
//            //Server is running always. This is done using this while(true) loop
//            while(true)
//            {
//                //Reading the message from the client
//                socket = serverSocket.accept();
//                InputStream is = socket.getInputStream();
//                InputStreamReader isr = new InputStreamReader(is);
//                BufferedReader br = new BufferedReader(isr);
//                String number = br.readLine();
//                System.out.println("Message received from client is "+number);
// 
//                //Multiplying the number by 2 and forming the return message
//                String returnMessage;
//                try
//                {
//                    int numberInIntFormat = Integer.parseInt(number);
//                    int returnValue = numberInIntFormat*2;
//                    returnMessage = String.valueOf(returnValue) + "\n";
//                }
//                catch(NumberFormatException e)
//                {
//                    //Input was not a number. Sending proper message back to client.
//                    returnMessage = "Please send a proper number\n";
//                }
// 
//                //Sending the response back to the client.
//                OutputStream os = socket.getOutputStream();
//                OutputStreamWriter osw = new OutputStreamWriter(os);
//                BufferedWriter bw = new BufferedWriter(osw);
//                bw.write(returnMessage);
//                System.out.println("Message sent to the client is "+returnMessage);
//                bw.flush();
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally
//        {
//            try
//            {
//                socket.close();
//            }
//            catch(Exception e){}
//        }
//    }
