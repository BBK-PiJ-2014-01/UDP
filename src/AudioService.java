import java.net.Socket;

/**
 * Created by Pierre on 02/04/2015.
 *
 * Interface definition for a audio server class
 *
 */

public interface AudioService {

    /**
     * Returns the client ID currently in first position and sending an audio file
     *
     * @return client sender unique ID.
     */
    String getClientSenderID();

    /**
     * Sets the client ID currently in first position and sending an audio file
     *
     * @param id the client sender unique ID.
     */
    void setClientSenderID(String id);

    /**
     * Returns the client socket
     *
     * @return the client socket
     */
    Socket getClientSocket();

    /**
     * Sets the client socket
     *
     * @param socket the client socket
     */
    void setClientSocket(Socket socket);

    /**
     * Returns if the client is in first position or not
     *
     * @return True (if client if first position), False (if client not in first position)
     */
    boolean getClientFirst();

    /**
     * Sets boolean value to:
     * - 'True' if the client is in first position
     * - 'False' if the client is not in first position
     *
     * @param position the client position
     */
    void setClientFirst(String position);

    /**
     * Generates a type 4 (pseudo randomly generated) Universally Unique ID
     * Checks if the client is in first position, and if so, stores its unique ID
     *
     * @return a universally unique ID.
     */
    String generateUniqueID();

    /**
     * Checks if the connecting client is in first position or not.
     * - if the client is in first position, the client-server messaging protocol reply is "FIRST"
     * - if the client is not in first position, the client-server messaging protocol reply is "NOT FIRST"
     *
     * @param requestingClientID ID of client requesting its position
     * @return the position of the client.
     */
    String indicateClientPosition(String requestingClientID);

    /**
     * Indicates what communication protocol should be used to transfer files : UDP
     * The client-server messaging protocol reply is "UDP"
     *
     * @return the protocol message "UDP"
     */
    String indicateCommunicationProtocol();

    /**
     * Based on the connected client position:
     * - relays an audio file to client if client is not in first position
     * - listens for an audio file from client if client is in first position
     */
    void audioFileTransfer();
}
