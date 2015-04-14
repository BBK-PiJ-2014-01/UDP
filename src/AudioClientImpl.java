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
    public void sendAudio(File audioFile, int packetSize) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] dataToTransfer = FileFactory.toByteArray(audioFile);
            byte[] interimPacket = null;
            byte[] receiveData = new byte[1024];

            //int packetCountMax = dataToTransfer.length / packetSize;
            //System.out.println(packetCountMax);

            boolean finished = false;
            int i=0;
            do {
                    interimPacket = Arrays.copyOfRange(dataToTransfer, i*packetSize, (i+1)*packetSize);
                    DatagramPacket sendPacket = new DatagramPacket(interimPacket, interimPacket.length, IPAddress, 5000);
                    clientSocket.send(sendPacket);
                    if (i*packetSize > dataToTransfer.length)
                        finished = true;
            }while(!finished);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData());
                System.out.println("FROM SERVER:" + modifiedSentence);

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
