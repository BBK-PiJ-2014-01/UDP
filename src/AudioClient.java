/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.File;

public interface AudioClient {

    long requestID();

    boolean firstToConnect();

    void sendAudio(File audioFile);

    File receiveAudio();

    void playAudio(File audioFile);

}
