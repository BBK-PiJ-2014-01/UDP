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
            toServerStream.writeUTF(ac.requestUniqueID());
            File audioFile = new File("./firetrucks.wav");
            ac.playAudio(audioFile);

            /*
            fromServerStream.close();
            toServerStream.close();
            client.close();
            */
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
