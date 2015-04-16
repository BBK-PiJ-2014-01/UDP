/**
 * Created by Pierre on 03/04/2015.
 */

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class AudioClientImpl implements AudioClient {

    private String clientID;

    public String getClientID() {
        return(clientID);
    }

    public void setClientID(String id) {
        clientID = id;
    }

    public AudioClientImpl(){
        setClientID("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String requestUniqueID() {
        String message = "requestUUID";
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String firstToConnect() {
        String message = "requestROLE"+getClientID();
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAudio(File audioFile, int packetSize, int maxAttempt) {

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

    /**
     * {@inheritDoc}
     */
    @Override
    public File receiveAudio() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void playAudio(File audioFile) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(audioFile));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        } catch (UnsupportedAudioFileException ex) {
            System.out.println(ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
