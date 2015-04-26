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
     * Generates a type 4 (pseudo randomly generated) Universally Unique ID
     *
     * @return a universally unique ID.
     */
    String generateUniqueID();

}
