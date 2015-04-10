/**
 * Created by Pierre on 02/04/2015.
 */

import java.util.UUID;

public class AudioServer implements AudioService {

    private String clientSenderID;

    public String getClientSenderID() {
        return(clientSenderID);
    }

    public void setClientSenderID(String id) {
        clientSenderID = id;
    }

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
