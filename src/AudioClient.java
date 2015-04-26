/**
 * Created by Pierre on 03/04/2015.
 *
 * Interface definition for a client class connecting to the Audio server
 * The client will either send or receive an audio file (.wav format) depending on whether it is
 * first to connect to the service.
 *
 * Three sets of methods:
 * - Getter and setter for the Unique client ID provided by the server
 *      getClientID(), setClientID()
 * - Methods supporting the communication protocol with the server
 *      requestUniqueID(), firstToConnect(), getProtocol()
 * - Method to play audio files (.wav format) received from the server
 *      playAudio()
 */

import java.io.File;

public interface AudioClient {

    /**
     * Returns the client universally unique ID
     *
     * @return client unique ID.
     */
    String getClientID();

    /**
     * Sets the client universally unique ID
     *
     * @param id the client unique ID.
     */
    void setClientID(String id);

    /**
     * Requests for a universally unique ID
     * The client-server messaging protocol command is "requestUUID"
     *
     * @return message request for a universally unique ID.
     */
    String requestUniqueID();

    /**
     * Requests which process (sending or receiving) is assigned, based on first to connect to the server or not.
     * The client-server messaging protocol command is "requestROLE" followed by the universally unique client ID.
     *
     * If the client is first to connect, it is assigned a sender process
     * If the client is not first to connect, it is assigned a receiver process
     *
     * @return message request for role assignment.
     */
    String firstToConnect();

    /**
     * Requests which communication protocol to use for sending/receiving audio files
     * The client-server messaging protocol command is "getPROTOCOL"
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
