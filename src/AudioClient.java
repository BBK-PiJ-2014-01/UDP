/**
 * Created by Pierre on 03/04/2015.
 */

import java.io.File;

public interface AudioClient {

    /**
     * Returns the client unique ID
     *
     * @return client unique ID.
     */
    String getClientID();

    /**
     * Sets the client unique ID
     *
     * @param id the client unique ID.
     */
    void setClientID(String id);

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

    /**
     * Requests which communication protocol to use for sending/receiving audio files
     *
     * @return message request for communication protocol to use.
     */
    String getProtocol();

    /**
     * Plays an audio file (Format .WAV)
     *
     * @param audioFile audio file to be played by the client
     */
    void playAudio(File audioFile);

}
