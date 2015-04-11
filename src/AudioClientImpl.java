/**
 * Created by Pierre on 03/04/2015.
 */

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

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
        String message = "requestROLE"+clientID;
        return(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAudio(File audioFile) {

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
