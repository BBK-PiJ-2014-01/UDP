
/**
 * Created by Pierre on 02/04/2015
 * Implementation of the Audio Service listening to multiple clients.
 *
 * First client to connect sends a looping audio recording to the server
 * The server relays the audio stream to all the clients who connect after this.
 */

import java.io.File;

public interface AudioService {

    void listenerUDP();

    String generateUniqueID();

}
