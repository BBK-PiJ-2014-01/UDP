/**
 * Created by Pierre on 03/04/2015.
 *
 * Implementation of the Interface AudioClient.
 */

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class AudioClientImpl implements AudioClient {

    private String clientID;

    /**
     * {@inheritDoc}
     */
    public String getClientID() {
        return(clientID);
    }

    /**
     * {@inheritDoc}
     */
    public void setClientID(String id) {
        clientID = id;
    }

    /**
     * Constructor for the class AudioClientImpl
     * Initialise the client ID to 'blank'
     */
    public AudioClientImpl(){
        setClientID("");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String requestUniqueID() {
        System.out.println("[Request to Server] Unique ID?...");
        String message = "requestUUID";
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String firstToConnect() {
        System.out.println("[Request to Server] Connection position?...");
        String message = "requestROLE"+getClientID();
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getProtocol() {
        System.out.println("[Request to Server] Protocol?...");
        String message = "getPROTOCOL";
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String notifyClosingConnection() {
        System.out.println("[Message to Server] Closing connection");
        String message = "closeCONNECTION";
        return(message);
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
