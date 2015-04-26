/**
 * Created by Pierre on 14/04/2015.
 *
 * Server class handling the connection with a client.
 * Each client connection is handled as a thread (Runnable interface implemented)
 *
 * Communication over TCP/IP except for transferring files when UDP protocol is used
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.util.UUID;

public class AudioServerClientHandler implements Runnable, AudioService {

    private File relayedAudioFile = new File("./relayedFile.wav");
    private File nextRelayedAudioFile;

    private Socket clientSocket = null;
    private boolean clientFirst;

    // Static variable as only one client sender at all times, regardless of the number of threads
    private static String clientSenderID = null;

    /**
     * {@inheritDoc}
     */
    public String getClientSenderID() {
        return(clientSenderID);
    }

    /**
     * {@inheritDoc}
     */
    public void setClientSenderID(String id) {
        clientSenderID = id;
    }

    /**
     * {@inheritDoc}
     */
    public Socket getClientSocket() {
        return(clientSocket);
    }

    /**
     * {@inheritDoc}
     */
    public void setClientSocket(Socket socket) {
        clientSocket = socket;
    }

    /**
     * {@inheritDoc}
     */
    public boolean getClientFirst() {
        return(clientFirst);
    }

    /**
     * {@inheritDoc}
     */
    public void setClientFirst(String position) {
        clientFirst = position.equals("FIRST");
    }

    /**
     * Constructor for the class AudioServerClientHandler
     * Initialise the client sender ID to 'blank', clientFirst to 'False' and keeps track of the client socket
     *
     * @param clientSocket socket the client is connected to
     */
    public AudioServerClientHandler(Socket clientSocket) {
        setClientSenderID("");
        setClientSocket(clientSocket);
        clientFirst = false;
    }

    /**
     * Method handling the communication with the connected client over TCP
     */
    @Override
    public void run() {
        // Establishing input and output streams for communication with the client
        try(DataInputStream fromClientStream = new DataInputStream(getClientSocket().getInputStream());
            DataOutputStream toClientStream = new DataOutputStream(getClientSocket().getOutputStream())) {

            String incomingMessage = "";

            while (!incomingMessage.equals("closeCONNECTION")) {

                incomingMessage = fromClientStream.readUTF();

                // Reply with UUID to client request for a unique ID
                if (incomingMessage.substring(0, 11).equals("requestUUID"))
                    toClientStream.writeUTF(generateUniqueID());

                // Reply with position ("FIRST" or "NOT FIRST") to client request for position
                if (incomingMessage.substring(0, 11).equals("requestROLE")) {
                    toClientStream.writeUTF(indicateClientPosition(incomingMessage.substring(11)));
                }

                // Reply with message "UDP" to client request for file transfer communication protocol
                if (incomingMessage.substring(0, 11).equals("getPROTOCOL")) {
                    toClientStream.writeUTF(indicateCommunicationProtocol());
                    // Opens UDP connection and sends/receives file depending on client position
                    audioFileTransfer();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * Method synchronized to make sure than only one thread at a time has access and can set the client sender Id
     */
    @Override
    public synchronized String generateUniqueID() {
        UUID uniqueID = UUID.randomUUID();
        String uniqueIDString = uniqueID.toString();
        System.out.println("Client ID assigned: " + uniqueIDString);
        if (getClientSenderID().equals(""))
            setClientSenderID(uniqueIDString);
        return (uniqueIDString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String indicateClientPosition(String requestingClientID) {
        String replyMessage;
        if (requestingClientID.equals(getClientSenderID())) {
            replyMessage = "FIRST";
        } else
            replyMessage = "NOT FIRST";
        System.out.println("Client Id="+requestingClientID+" position: "+replyMessage);
        setClientFirst(replyMessage);
        return(replyMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String indicateCommunicationProtocol() {
        String replyMessage = "UDP";
        System.out.println("Protocol requested: " + replyMessage);
        return(replyMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void audioFileTransfer() {
        if (getClientFirst()) {
            nextRelayedAudioFile = UDPFileTransfer.receive();
            try {
                Files.deleteIfExists(relayedAudioFile.toPath());
                Files.copy(nextRelayedAudioFile.toPath(),relayedAudioFile.toPath());

            } catch(IOException ex) {
                ex.printStackTrace();
            }
            setClientSenderID("");
        } else {
            UDPFileTransfer.send(relayedAudioFile);
        }
    }
}
