/**
 * Created by Pierre on 26/04/2015.
 */

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestServerProtocol {

    final static String expectedCommunicationProtocolMessage = "UDP";
    final static String expectedClientPositionMessageWhenFirst = "FIRST";
    final static String expectedClientPositionMessageWhenNotFirst = "NOT FIRST";

    private static AudioServerClientHandler clientHandler;

    @BeforeClass
    public static void buildUp() {
        clientHandler = new AudioServerClientHandler(null);
    }

    @Test
    public void tests_indicateClientPosition_SendsCorrectProtocolMessage() {
        clientHandler.setClientSenderID("067e6162-3b6f-4ae2-a171-2470b63dff00");
        String actualMessage1 = clientHandler.indicateClientPosition("067e6162-3b6f-4ae2-a171-2470b63dff00");
        assertEquals("Invalid position message",
                expectedClientPositionMessageWhenFirst, actualMessage1);
        String actualMessage2 = clientHandler.indicateClientPosition("043e6262-1c2f-4bn2-a534-1570b12deb00");
        assertEquals("Invalid position message",
                expectedClientPositionMessageWhenNotFirst, actualMessage2);
    }

    @Test
    public void tests_generateUUID_GeneratesCorrectIdFormat() {
        String actualMessage = clientHandler.generateUniqueID();
        int expectedLength = 36;
        String expectedCharacter = "-";
        assertEquals("UUID's length is not correct", expectedLength, actualMessage.length());
        assertEquals("Invalid UUID Format - Expected character '-'",
                expectedCharacter, actualMessage.substring(8,9));
        assertEquals("Invalid UUID Format - Expected character '-'",
                expectedCharacter, actualMessage.substring(13,14));
        assertEquals("Invalid UUID Format - Expected character '-'",
                expectedCharacter, actualMessage.substring(18,19));
        assertEquals("Invalid UUID Format - Expected character '-'",
                expectedCharacter, actualMessage.substring(23, 24));
        assertNotEquals("Invalid UUID Format - Did not expect character '-'",
                expectedCharacter, actualMessage.substring(30,31));
    }

    @Test
    public void tests_indicateCommunicationProtocol_SendsCorrectProtocolMessage() {
        String actualMessage = clientHandler.indicateCommunicationProtocol();
        assertEquals("Invalid protocol message sent when requested communication protocol",
                expectedCommunicationProtocolMessage, actualMessage);
    }
}