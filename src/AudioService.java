/**
 * Created by Pierre on 02/04/2015
 * Implementation of the Audio Service listening to multiple clients.
 *
 * First client to connect sends a looping audio recording to the server
 * The server relays the audio stream to all the clients who connect after this.
 */

public interface AudioService {

    void listenerTCP();

    void listenerUDP();

    int generatorID();

}
