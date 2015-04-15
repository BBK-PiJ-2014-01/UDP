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

    private File relayFile = null;
    private File toBeRelayedFile = null;

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

            String line = "";
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
                    String position = null;
                    if (requestingClientID.equals(getClientSenderID())) {
                        position = "1";
                        toClientStream.writeUTF(position);
                        File receivedFile = new File("./new.wav");
                        receivedFile = receiveAudioFile();
                    } else {
                        position = "0";
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public File receiveAudioFile() {
        File receivedFile = null;
        byte[] dataToReceive = null;

        try(DatagramSocket serverSocket = new DatagramSocket(5000)) {

            byte[] clientMessage;
            String fromClientTrimmed;
            do {
                byte[] receiveData = new byte[64000];
                byte[] sendData;
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String fromClient = new String(receivePacket.getData());
                fromClientTrimmed = fromClient.trim();
                clientMessage = fromClientTrimmed.getBytes();
                System.out.println(clientMessage.length);

                if (!fromClientTrimmed.equals("COMPLETED")) {
                    if ((dataToReceive != null)) {
                        byte[] interim = dataToReceive;
                        dataToReceive = new byte[dataToReceive.length + clientMessage.length];
                        System.arraycopy(interim, 0, dataToReceive, 0, interim.length);
                        System.arraycopy(clientMessage, 0, dataToReceive, interim.length, clientMessage.length);
                    } else
                        dataToReceive = clientMessage;
                }
                System.out.println("cumul: " + dataToReceive.length);
                receivedFile = FileFactory.fromByteArray(dataToReceive,"./new.wav");
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String serverMessage = "ACKNOWLEDGED";
                sendData = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);


            } while (!fromClientTrimmed.equals("COMPLETED"));
            System.out.println("File uploaded");
        }catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return(receivedFile);
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
