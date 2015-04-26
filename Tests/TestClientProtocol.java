/**
 * Created by Pierre on 26/04/2015.
 */

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestClientProtocol {

    final static String expectedRequestIdMessage = "requestUUID";
    final static String expectedRequestPositionMessage = "requestROLE067e6162-3b6f-4ae2-a171-2470b63dff00";
    final static String expectedRequestCommunicationProtocolMessage = "getPROTOCOL";
    final static String expectedMessageConnectionClosure = "closeCONNECTION";

    private static AudioClient ac;

    @BeforeClass
    public static void buildUp() {
        ac = new AudioClientImpl();
        ac.setClientID("067e6162-3b6f-4ae2-a171-2470b63dff00");
    }

    @Test
    public void tests_requestUniqueID_SendsCorrectProtocolMessage() {
        String actualMessage = ac.requestUniqueID();
        assertEquals("Invalid protocol message when requesting UUID",
                expectedRequestIdMessage, actualMessage);
    }

    @Test
     public void tests_firstToConnect_SendsCorrectProtocolMessage() {
        String actualMessage = ac.firstToConnect();
        assertEquals("Invalid protocol message when requesting position",
                expectedRequestPositionMessage, actualMessage);
    }

    @Test
     public void tests_getProtocol_SendsCorrectProtocolMessage() {
        String actualMessage = ac.getProtocol();
        assertEquals("Invalid protocol message when requesting communication protocol",
                expectedRequestCommunicationProtocolMessage, actualMessage);
    }

    @Test
    public void tests_notifyClosingConnection_SendsCorrectProtocolMessage() {
        String actualMessage = ac.notifyClosingConnection();
        assertEquals("Invalid protocol message when requesting communication protocol",
                expectedMessageConnectionClosure, actualMessage);
    }
}
