/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServerLauncher {
    public static void main(String[] args) {
        AudioServerLauncher asl = new AudioServerLauncher();
        asl.launch();
    }

    private void launch(){
        // Creating the server object
        AudioService as = new AudioServer();
        // Opening connection to clients on Port 2000
        try(ServerSocket server = new ServerSocket(2000)) {
            System.out.println("AudioServer started...");
            // Waiting for the client connection
            try(Socket client = server.accept();
                // Establishing input and output streams for communication with the client
                DataInputStream fromClientStream = new DataInputStream(client.getInputStream());
                DataOutputStream toClientStream = new DataOutputStream(client.getOutputStream()))
            {
                while (true) {
                    String line = fromClientStream.readUTF();
                    if (line.equals("requestUID")) {
                        String uniqueID = as.generateUniqueID();
                        toClientStream.writeUTF(uniqueID);
                        System.out.println(uniqueID);
                    }
                }
            } catch (IOException ex) {
                ex.getMessage();
            }
        } catch (IOException ex) {
            ex.getMessage();
        }

    }

}
