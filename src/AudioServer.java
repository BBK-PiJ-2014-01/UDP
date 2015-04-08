/**
 * Created by Pierre on 02/04/2015.
 */

import java.util.UUID;

public class AudioServer implements AudioService {


    @Override
    public void listenerTCP() {

    }

    @Override
    public void listenerUDP() {

    }

    @Override
    public String generateUniqueID() {
        UUID uniqueID = UUID.randomUUID();
        return (uniqueID.toString());
    }
}
