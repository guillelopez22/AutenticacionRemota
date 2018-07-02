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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.bson.Document;

public class Auth_Server {

    private static Socket socket;
    private static Queue<String> credentials = new LinkedList<>();
    private static MongoClientURI uri = new MongoClientURI("mongodb://admin:admin123@ds123151.mlab.com:23151/remote_auth");
    private static MongoClient client = new MongoClient(uri);
    private static MongoDatabase db = client.getDatabase(uri.getDatabase());
    private static MongoCollection<Document> users = db.getCollection("users");

    public static void main(String[] args) {
        try {

            int port = 25000;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port 25000");

            //Server is running always. This is done using this while(true) loop
            while (true) {
                //Reading the message from the client
                socket = serverSocket.accept();
                String username = "", password = "";
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String message = br.readLine();
                credentials.add(message);

                try {
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
                    System.out.println("asd");
                    if (bw != null) {
                        System.out.println("si hay");
                        bw.flush();
                    }else{
                        System.out.println("no hay nada");
                    }
                    System.out.println("Message sent to the server : " + sendMessage);

                } catch (IOException exception) {
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
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
