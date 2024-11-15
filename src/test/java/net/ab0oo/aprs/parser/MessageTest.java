package net.ab0oo.aprs.parser;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MessageTest {
    APRSPacket validMessagePacket;
    private APRSPacket validMessageRejectionPacket;
    private APRSPacket validMessageAcceptPacket;

    @BeforeEach
    void prepare() throws Exception {
        validMessagePacket = Parser.parse("CALL-7>APY05D,WIDE1-1,WIDE2-1,qAR,TARGET::TARGET   :test ok{1");
        validMessageRejectionPacket = Parser.parse("CALL-7>APY05D,WIDE1-1,WIDE2-1,qAR,TARGET::TARGET   :rej{1");
        validMessageAcceptPacket = Parser.parse("CALL-7>APY05D,WIDE1-1,WIDE2-1,qAR,TARGET::TARGET   :ack{1");

    }

    @Test
    void messageTest() {
        InformationField field = validMessagePacket.getAprsInformation();
        APRSData mp = null;
        if ( field.containsType(APRSTypes.T_MESSAGE) ) {
            mp = field.getAprsData(APRSTypes.T_MESSAGE);
        }
        assertInstanceOf(MessagePacket.class, mp,
                "Expecting the AprsInfo is instance of MessagePacket");
        assertEquals("test ok", ((MessagePacket) mp).getMessageBody());
        assertEquals("1", ((MessagePacket) mp).getMessageNumber());
        assertFalse(((MessagePacket) mp).isAck());
        assertFalse(((MessagePacket) mp).isRej());
    }

    @Test
    void arcMessageTest() {
        InformationField field = validMessageAcceptPacket.getAprsInformation();
        APRSData mp = null;
        if ( field.containsType(APRSTypes.T_MESSAGE) ) {
            mp = field.getAprsData(APRSTypes.T_MESSAGE);
        }
        assertInstanceOf(MessagePacket.class, mp,
                "Expecting the AprsInfo is instance of MessagePacket");
        assertTrue(((MessagePacket) mp).isAck());
        assertFalse(((MessagePacket) mp).isRej());
    }

    @Test
    void rejMessageTest() {
        InformationField field = validMessageRejectionPacket.getAprsInformation();
        APRSData mp = null;
        if ( field.containsType(APRSTypes.T_MESSAGE) ) {
            mp = field.getAprsData(APRSTypes.T_MESSAGE);
        }
        assertInstanceOf(MessagePacket.class, mp,
                "Expecting the AprsInfo is instance of MessagePacket");
        assertTrue(((MessagePacket) mp).isRej());
        assertFalse(((MessagePacket) mp).isAck());
    }

}
