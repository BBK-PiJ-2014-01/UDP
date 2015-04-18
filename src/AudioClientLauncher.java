/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class AudioClientLauncher {
    public static void main(String[] args) {
        AudioClientLauncher acl = new AudioClientLauncher();
        acl.launch();
    }

    private void launch() {
        try {
            // Creating the client object
            AudioClient ac = new AudioClientImpl();
            // Opening connection to server on Port 2000
            Socket client = new Socket("localhost", 2000);
            // Establishing input and output streams for communication with the server
            DataInputStream fromServerStream = new DataInputStream(client.getInputStream());
            DataOutputStream toServerStream = new DataOutputStream(client.getOutputStream());
            // Requests a unique ID
            System.out.println("[Request Server] Unique ID...");
            toServerStream.writeUTF(ac.requestUniqueID());
            ac.setClientID(fromServerStream.readUTF());
            System.out.println("[Response Server] Assigned ID: "+ac.getClientID());
            // Requests if first to connect
            System.out.println("[Request Server] Connection position...");
            toServerStream.writeUTF(ac.firstToConnect());
            String position = fromServerStream.readUTF();
            System.out.println("[Response Server] Position: "+position);


            if (position.equals("1")) {
                File audioFile = new File("C:/firetrucks.wav");
                UDPFileTransfer.send(audioFile,1024*32,5);
            }

            toServerStream.writeUTF("closeCONNECTION");
            fromServerStream.close();
            toServerStream.close();
            client.close();

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
