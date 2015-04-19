/**
 * Created by Pierre on 18/04/2015.
 */

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public interface UDPFileTransfer {

    final int maxSendingAttempt = 5;

    static void send(File audioFile, int packetSize, int maxAttempt) {

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] dataToTransfer = FileFactory.toByteArray(audioFile);
            byte[] interimPacket = null;
            byte[] sendData = null;
            byte[] receiveData = new byte[12];
            boolean finished = false;
            int i=0;
            do {
                if(((i+1)*packetSize) <= dataToTransfer.length)
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*packetSize, (i+1)*packetSize);
                else
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*packetSize, dataToTransfer.length);
                byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(interimPacket.length).array();
                System.out.println(interimPacket.length);
                sendData = FileFactory.concatenateByteArrays(interimPacketLength,interimPacket);
                String serverReply = null;
                int attempt = 0;
                do {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
                    clientSocket.send(sendPacket);
                    attempt++;
                    System.out.println(attempt);
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    clientSocket.receive(receivePacket);
                    serverReply = new String(receivePacket.getData());
                    System.out.println("FROM SERVER:" + serverReply);
                } while(!serverReply.equals("ACKNOWLEDGED"));
                if ((i+1)*packetSize > dataToTransfer.length)
                    finished = true;
                i++;
            }while(!finished);

            String message = "COMPLETED";
            byte[] clientMessage = message.getBytes();
            byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(clientMessage.length).array();
            sendData = FileFactory.concatenateByteArrays(interimPacketLength,clientMessage);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
            clientSocket.send(sendPacket);
            System.out.println("File uploaded!");

        } catch (SocketException ex) {
            System.out.println(ex.getMessage());
        } catch (UnknownHostException ex) {
            System.out.println("Host exception");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    static File receive() {
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
                byte[] fromClient = receivePacket.getData();
                byte[] dataPacketSize = Arrays.copyOfRange(fromClient, 0, 4);
                int dataSize = ByteBuffer.wrap(dataPacketSize).getInt();
                clientMessage = Arrays.copyOfRange(fromClient,4,dataSize+4);
                fromClientTrimmed= new String(clientMessage);
                if (!fromClientTrimmed.equals("COMPLETED")) {
                    if ((dataToReceive != null)) {
                        byte[] interim = dataToReceive;
                        dataToReceive = FileFactory.concatenateByteArrays(interim,clientMessage);
                    } else
                        dataToReceive = clientMessage;
                }
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String serverMessage = "ACKNOWLEDGED";
                sendData = serverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                serverSocket.send(sendPacket);


            } while (!fromClientTrimmed.equals("COMPLETED"));
            System.out.println("File uploaded: Size: "+dataToReceive.length);
            receivedFile = FileFactory.fromByteArray(dataToReceive,"./new.wav");
        }catch (SocketException ex) {
            System.out.println(ex.getMessage());
        }catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return(receivedFile);
    }

}