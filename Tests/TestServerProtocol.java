/**
 * Created by Pierre on 26/04/2015.
 */


import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestServerProtocol {

    final static String expectedCommunicationProtocolMessage = "UDP";

    private static AudioServerClientHandler clientHandler;

    @BeforeClass
    public static void buildUp() {
        clientHandler = new AudioServerClientHandler(null);
    }

    @Test
    public void tests_indicateCommunicationProtocol_SendsCorrectProtocolMessage() {
        String actualMessage = clientHandler.indicateCommunicationProtocol();
        assertEquals("Invalid protocol message sent when requested communication protocol",
                expectedCommunicationProtocolMessage, actualMessage);
    }
}