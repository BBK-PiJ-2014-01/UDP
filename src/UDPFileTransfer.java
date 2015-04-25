/**
 * Created by Pierre on 18/04/2015.
 */

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public interface UDPFileTransfer {

    final int maxSendingAttempt = 3;
    final int packetSize = 1024 * 32;

    static void send(File audioFile) {

        try (DatagramSocket sendingSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] dataToTransfer = FileFactory.toByteArray(audioFile);
            byte[] interimPacket;
            byte[] sendData;
            byte[] receiveData = new byte[12];
            boolean finished = false;
            int i=0;
            do {
                if(((i+1)*packetSize) <= dataToTransfer.length)
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*packetSize, (i+1)*packetSize);
                else
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*packetSize, dataToTransfer.length);
                byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(interimPacket.length).array();
                System.out.println("Packet length: "+interimPacket.length);
                sendData = FileFactory.concatenateByteArrays(interimPacketLength,interimPacket);
                String receiverReply = null;
                int attempt = 0;
                do {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
                    sendingSocket.send(sendPacket);
                    attempt++;
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    sendingSocket.receive(receivePacket);
                    receiverReply = new String(receivePacket.getData());
                    System.out.println("FROM RECEIVER:" + receiverReply);
                } while(!receiverReply.equals("ACKNOWLEDGED")||(attempt == maxSendingAttempt));
                if ((i+1)*packetSize > dataToTransfer.length)
                    finished = true;
                i++;
            }while(!finished);

            String message = "COMPLETED";
            byte[] clientMessage = message.getBytes();
            byte[] interimPacketLength = ByteBuffer.allocate(4).putInt(clientMessage.length).array();
            sendData = FileFactory.concatenateByteArrays(interimPacketLength,clientMessage);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 5000);
            sendingSocket.send(sendPacket);
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

        try(DatagramSocket receivingSocket = new DatagramSocket(5000)) {

            byte[] senderMessage;
            String fromSenderTrimmed;
            do {
                byte[] receiveData = new byte[64000];
                byte[] sendData;
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                receivingSocket.receive(receivePacket);
                byte[] fromSender = receivePacket.getData();
                byte[] dataPacketSize = Arrays.copyOfRange(fromSender, 0, 4);
                int dataSize = ByteBuffer.wrap(dataPacketSize).getInt();
                senderMessage = Arrays.copyOfRange(fromSender,4,dataSize+4);
                fromSenderTrimmed= new String(senderMessage);
                if (!fromSenderTrimmed.equals("COMPLETED")) {
                    if ((dataToReceive != null)) {
                        byte[] interim = dataToReceive;
                        dataToReceive = FileFactory.concatenateByteArrays(interim,senderMessage);
                    } else
                        dataToReceive = senderMessage;
                }
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                String receiverMessage = "ACKNOWLEDGED";
                sendData = receiverMessage.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                receivingSocket.send(sendPacket);


            } while (!fromSenderTrimmed.equals("COMPLETED"));
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
