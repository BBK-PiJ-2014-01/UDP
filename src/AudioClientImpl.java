/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class AudioClientImpl implements AudioClient {

    @Override
    public String requestUniqueID() {
        String message = "requestUID";
        return(message);
    }

    @Override
    public boolean firstToConnect() {
        return false;
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
