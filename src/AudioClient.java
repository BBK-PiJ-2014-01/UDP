/**
 * Created by Pierre on 03/04/2015.
 *
 * Interface definition for a client class connecting to the Audio server
 * The client will either send or receive an audio file (.wav format) depending on whether it is
 * first to connect to the service.
 *
 * Four sets of methods:
 * - Getter and setter for the Unique client ID provided by the server
 *      getClientID(), setClientID()
 * - Methods supporting the messaging protocol with the server over TCP/IP
 *      requestUniqueID(), firstToConnect(), getProtocol(), notifyClosingConnection()
 * - Method supporting the transfer of audio files with the server over UDP
 *      audioFileTransfer()
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
     * Notifies that the client connection will be closed
     * The client-server messaging protocol command is "closeCONNECTION"
     *
     * @return message notification of connection closure.
     */
    String notifyClosingConnection();

    /**
     * Plays an audio file (Format .WAV)
     *
     * @param audioFile audio file to be played by the client
     */
    void playAudio(File audioFile);

    /**
     * Based on its position in the server queue, the client:
     * - sends an audio file to server if server notified client, it was first to connect (protocol message =  "FIRST")
     * - listens for an audio file from server if server notified client it was not first to connect (protocol message "NOT FIRST")
     * Once an audio file has been fully received, the client plays it.
     *
     * In its current version, the client can only handle UDP communication protocol for transferring files.
     * If the server requested another protocol, the client outputs an error message.
     *
     * @param protocol communication protocol the client is required to use to transfer files
     * @param position client position
     */
    void audioFileTransfer(String protocol, String position);

}
