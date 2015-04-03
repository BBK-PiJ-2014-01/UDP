/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AudioClientLauncher {
    public static void main(String[] args) {
        AudioClientLauncher acl = new AudioClientLauncher();
        acl.launch();
    }

    private void launch() {
        try {
            // Opening connection to server on Port 2000
            Socket socket = new Socket("localhost", 2000);
            // Establishing input and output streams for communication with the server
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            //long id = requestID();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
