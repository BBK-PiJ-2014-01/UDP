/**
 * Created by Pierre on 14/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class AudioServerClientHandler implements Runnable, AudioService {

    private String clientSenderID = null;
    private Socket clientSocket = null;

    public String getClientSenderID() {
        return(clientSenderID);
    }

    public void setClientSenderID(String id) {
        clientSenderID = id;
    }

    public void setClientSocket(Socket socket) {
        clientSocket = socket;
    }

    public Socket getClientSocket() {
        return(clientSocket);
    }

    public AudioServerClientHandler(Socket clientSocket) {
        setClientSenderID("");
        setClientSocket(clientSocket);
    }

    @Override
    public void run() {
        // Establishing input and output streams for communication with the client
        try(DataInputStream fromClientStream = new DataInputStream(getClientSocket().getInputStream());
            DataOutputStream toClientStream = new DataOutputStream(getClientSocket().getOutputStream())) {

            String line = fromClientStream.readUTF();
            if (line.substring(0, 11).equals("requestUUID")) {
                String uniqueID = generateUniqueID();
                toClientStream.writeUTF(uniqueID);
                System.out.println("Client ID assigned: " + uniqueID);
                if (getClientSenderID().equals(""))
                    setClientSenderID(uniqueID);

            }
            if (line.substring(0, 11).equals("requestROLE")) {
                String requestingClientID = line.substring(11);
                String position = null;
                if (requestingClientID.equals(getClientSenderID()))
                    position = "1";
                else
                    position = "0";
                System.out.println(position);
                toClientStream.writeUTF(position);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void listenerTCP() {

    }

    @Override
    public void listenerUDP() {

    }

    @Override
    public String generateUniqueID() {
        UUID uniqueID = UUID.randomUUID();
        return (uniqueID.toString());
    }
}
