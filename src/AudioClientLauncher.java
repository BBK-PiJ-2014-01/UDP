/**
 * Created by Pierre on 03/04/2015.
 *
 * Audio client launcher class.
 * - Connects to server via TCP
 * - requests UUID
 * - requests Position in queue
 * - requests communication protocol for sending/receiving files (UDP only expected)
 * - Opens UDP connection and sends/receives audio files based on position in queue
 * - Disconnects from server when finished
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class AudioClientLauncher {

    final int PORT_NUMBER_TCP = 2000;

    public static void main(String[] args) {
        AudioClientLauncher acl = new AudioClientLauncher();
        acl.launch();
    }

    private void launch() {

        try (
            // Opening connection to server on Port 2000
            Socket client = new Socket("localhost", PORT_NUMBER_TCP);
            // Establishing input and output streams for communication with the server
            DataInputStream fromServerStream = new DataInputStream(client.getInputStream());
            DataOutputStream toServerStream = new DataOutputStream(client.getOutputStream());)
            {

            // Creating the client object
            AudioClient ac = new AudioClientImpl();

            // STEP1: Requests the server a universally unique ID
            toServerStream.writeUTF(ac.requestUniqueID());
            ac.setClientID(fromServerStream.readUTF());
            System.out.println("[Response Server] Assigned ID: "+ac.getClientID());

            // STEP2: Requests the server if first to connect
            toServerStream.writeUTF(ac.firstToConnect());
            String position = fromServerStream.readUTF();
            System.out.println("[Response Server] Position: "+position);

            // STEP3: Requests the server which communication protocol to use for file transfer
            toServerStream.writeUTF(ac.getProtocol());
            String protocol = fromServerStream.readUTF();
            System.out.println("[Response Server] Protocol: "+protocol);

            // STEP4: Sends or receives audio file based on position & communication protocol provided by the server
            ac.audioFileTransfer(protocol, position);

            // STEP5: Notifies the server the end of the client connection
            toServerStream.writeUTF(ac.notifyClosingConnection());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
