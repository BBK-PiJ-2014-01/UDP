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

    /**
     * Requests which process (sending or receiving) is assigned, based on first to connect to the server or not.
     * If the client is first to connect, it is assigned a sender process
     * If the client is not first to connect, it is assigned a receiver process
     *
     * @return message request for role assignment.
     */
    String firstToConnect();

    void sendAudio(File audioFile);

    File receiveAudio();

    /**
     * Plays an audio file (Format .WAV)
     *
     * @param audioFile audio file to be played by the client
     */
    void playAudio(File audioFile);

}
