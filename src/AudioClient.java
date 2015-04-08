/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.File;

public interface AudioClient {

    /**
     * Requests for a unique ID
     *
     * @return message request for a unique ID.
     */
    String requestUniqueID();

    boolean firstToConnect();

    void sendAudio(File audioFile);

    File receiveAudio();

    void playAudio(File audioFile);

}
