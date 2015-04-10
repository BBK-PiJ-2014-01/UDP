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

    /**
     * {@inheritDoc}
     */
    @Override
    public String requestUniqueID() {
        String message = "requestUID";
        return(message);
    }

    @Override
    public String firstToConnect() {
        String message = "requestRole";
        return(message);
    }

    @Override
    public void sendAudio(File audioFile) {

    }

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
