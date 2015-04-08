/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.File;

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

    @Override
    public void playAudio(File audioFile) {

    }
}
