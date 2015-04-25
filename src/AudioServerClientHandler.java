/**
 * Created by Pierre on 14/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.UUID;

public class AudioServerClientHandler implements Runnable, AudioService {

    private File relayedAudioFile = new File("./relayedFile.wav");
    private File nextRelayedAudioFile = new File("./nextRelayedFile.wav");

    private String clientSenderID = null;
    private Socket clientSocket = null;

    public String getClientSenderID() {
        return(clientSenderID);
    }

    public void setClientSenderID(String id) {
        clientSenderID = id;
    }

    /**
     * Sets the client socket
     *
     * @param socket the client socket
     */
    public void setClientSocket(Socket socket) {
        clientSocket = socket;
    }

    /**
     * Returns the client socket
     *
     * @return the client socket
     */
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

            String line = "";
            String clientPosition = "";
            String replyMessage = "";

            while (!line.equals("closeCONNECTION")) {

                line = fromClientStream.readUTF();

                if (line.substring(0, 11).equals("requestUUID")) {
                    String uniqueID = generateUniqueID();
                    toClientStream.writeUTF(uniqueID);
                    System.out.println("Client ID assigned: " + uniqueID);
                    if (getClientSenderID().equals(""))
                        setClientSenderID(uniqueID);
                }
                if (line.substring(0, 11).equals("requestROLE")) {
                    String requestingClientID = line.substring(11);
                    if (requestingClientID.equals(getClientSenderID()))
                        clientPosition = "FIRST";
                    else
                        clientPosition = "NOT FIRST";
                    System.out.println("Client Id="+requestingClientID+" position: "+clientPosition);
                    toClientStream.writeUTF(clientPosition);
                }
                if (line.substring(0, 11).equals("getPROTOCOL")) {
                    replyMessage = "UDP";
                    toClientStream.writeUTF(replyMessage);
                    System.out.println("Protocol requested: " + replyMessage);
                    if (clientPosition.equals("FIRST")) {
                        nextRelayedAudioFile = UDPFileTransfer.receive();
                        setClientSenderID("");
                    } else {
                        UDPFileTransfer.send(relayedAudioFile);
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
