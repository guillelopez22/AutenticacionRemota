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
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.bson.Document;

public class Coordinador {

    private static Socket socket;
    public static String Rmessage;

    public static String main(String args[]) {
        try {
            String username = args[1], password = args[2];
            String host = "localhost";
            String message = "";
            int port = Integer.parseInt(args[0]);
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            //Send the message to the server
            message = username + "," + password + "\n";
            System.out.println(message);

            OutputStream os = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(message);
            bw.flush();
            System.out.println("Message sent to the server : " + message);

            //Get the return message from the server
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            Rmessage = br.readLine();
            System.out.println("Received: "+Rmessage);

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
        return Rmessage;
    }
}
