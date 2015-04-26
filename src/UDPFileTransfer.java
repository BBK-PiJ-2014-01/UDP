/**
 * Created by Pierre on 18/04/2015.
 *
 * Methods defined as static. Can be used by both clients and servers for sending/receiving files.
 *
 * Implementing static methods with full body is a new feature of Interfaces from Java7.
 */

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public interface UDPFileTransfer {

    final int MAX_SENDING_ATTEMPT = 3;  // Maximum number of attempts, the same data packet is sent to receiver
    final int LATENCY = 100;            // Time waited in milliseconds before checking the receiver acknowledgement
    final int PACKET_SIZE = 1024 * 32;  // Maximum size of a data packet
    final int PORT_NUMBER_UDP = 5000;   // Port number used for UDP connection

    /**
     * Sends a file in chunks of 32768 bytes to a receiver on "Local Host" - Port 5000, using the UDP protocol
     *
     * To enhance the quality of the UDP transmission(e.g. receiver receives all data packets and in order), the client:
     * - includes a 4 bytes header in the data packet, specifying the length of the data included in the packet
     * (data length is usually different in the last data packet transferred)
     * - waits for receipt acknowledgement from the receiver after each data packet
     * - attempts a maximum of three times sending the data packet again if no acknowledgement is received
     *
     * @param audioFile file transferred
     */
    static void send(File audioFile) {

        try (DatagramSocket sendingSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            // Converts audio file into a byte array
            byte[] dataToTransfer = FileFactory.toByteArray(audioFile);
            byte[] interimPacket;
            byte[] sendData;
            byte[] receiveData = new byte[12];
            boolean finished = false;   // Flag = true when the transfer is completed
            boolean errorFlag = false;  // Flag = true when the transfer may not have been successful
            int i=0;
            do {
                if(((i+1)*PACKET_SIZE) <= dataToTransfer.length)
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*PACKET_SIZE, (i+1)*PACKET_SIZE);
                else
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*PACKET_SIZE, dataToTransfer.length);
                // Build header with the length of the data byte array
                byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(interimPacket.length).array();
                System.out.println("Packet length: "+interimPacket.length);
                // Concatenate header & data byte arrays
                sendData = FileFactory.concatenateByteArrays(interimPacketLength,interimPacket);
                String receiverReply = null;
                int attempt = 0;
                // For each data packet, waits for 100ms.
                // seeks receiver acknowledgement. Attempts sending 3 times maximum if no acknowledgement.
                do {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT_NUMBER_UDP);
                    sendingSocket.send(sendPacket);
                    attempt++;
                    try {
                        Thread.sleep(LATENCY);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    sendingSocket.receive(receivePacket);
                    receiverReply = new String(receivePacket.getData());
                    System.out.println("FROM RECEIVER:" + receiverReply);
                    if (attempt == MAX_SENDING_ATTEMPT)
                        errorFlag = true;
                } while(!receiverReply.equals("ACKNOWLEDGED")||(attempt == MAX_SENDING_ATTEMPT));
                if ((i+1)*PACKET_SIZE > dataToTransfer.length)
                    finished = true;
                i++;
            }while(!finished);

            // Sends message to receiver that the transfer is completed
            String message = "COMPLETED";
            byte[] clientMessage = message.getBytes();
            byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(clientMessage.length).array();
            sendData = FileFactory.concatenateByteArrays(interimPacketLength,clientMessage);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, PORT_NUMBER_UDP);
            sendingSocket.send(sendPacket);
            // Warning sent if the maximum number of attempts was made to send the same packet
            if (errorFlag)
                System.out.println("File uploaded but latency issues may have corrupted it");
            else
                System.out.println("File uploaded!");

        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownHostException ex) {
            System.out.println("Host exception");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Receives a file from a sender on Port 5000, using the UDP protocol
     *
     * - Extracts the data part of the received packet, based on the info included in the packet header
     * - Acknowledges receipt of each packet received
     * - Concatenates all data packets received into a single byte array
     * - Converts the final version of the byte array back into an audio file
     *
     * @param filePath filePath to allocate to the received file
     * @return file received
     */
    static File receive(String filePath) {
        File receivedFile = null;
        byte[] dataToReceive = null;

        try(DatagramSocket receivingSocket = new DatagramSocket(PORT_NUMBER_UDP)) {

            byte[] senderMessage;
            String fromSenderTrimmed;
            do {
                byte[] receiveData = new byte[64000];
                byte[] sendData;
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receivingSocket.receive(receivePacket);
                byte[] fromSender = receivePacket.getData();
                // Identifies data size from header
                byte[] dataPacketSize = Arrays.copyOfRange(fromSender, 0, 4);
                int dataSize = ByteBuffer.wrap(dataPacketSize).getInt();
                // Separate header from data
                senderMessage = Arrays.copyOfRange(fromSender,4,dataSize+4);
                fromSenderTrimmed= new String(senderMessage);
                // Concatenate all data packets received so far
                if (!fromSenderTrimmed.equals("COMPLETED")) {
                    if ((dataToReceive != null)) {
                        byte[] interim = dataToReceive;
                        dataToReceive = FileFactory.concatenateByteArrays(interim,senderMessage);
                    } else
                        dataToReceive = senderMessage;
                }
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                // Confirms acknowledgement of packet received
                String receiverMessage = "ACKNOWLEDGED";
                sendData = receiverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                receivingSocket.send(sendPacket);
            } while (!fromSenderTrimmed.equals("COMPLETED"));
            System.out.println("File uploaded: Size: "+dataToReceive.length);
            // Converts data byte array back into a file
            receivedFile = FileFactory.fromByteArray(dataToReceive,filePath);
        }catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return(receivedFile);
    }

}
