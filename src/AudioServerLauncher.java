/**
 * Created by Pierre on 03/04/2015.
 * Audio server launcher.
 * Listen for TCP connection and place further handling of connected client in a separate thread
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioServerLauncher {

    public static void main(String[] args) {
        AudioServerLauncher asl = new AudioServerLauncher();
        asl.launch();
    }

    private void launch(){

        // Opening connection to clients on Port 2000
        try(ServerSocket server = new ServerSocket(2000)) {
            System.out.println("AudioServer started...");
            while (true) {
                Socket client = null;
                try {
                    // Waiting for the client connection
                    client = server.accept();
                    // Placing client connection in a separate thread
                    AudioServerClientHandler newClient = new AudioServerClientHandler(client);
                    Thread newThread = new Thread(newClient);
                    newThread.start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
