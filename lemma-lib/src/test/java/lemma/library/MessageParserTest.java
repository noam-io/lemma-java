package lemma.library;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageParserTest {
    @Test
    public void parsePoloMessage() {
        PoloMessage polo = MessageParser.parsePolo("[\"polo\",\"Room Name\", 1345]");
        assertEquals(1345, polo.portNumber);
        assertEquals("Room Name", polo.roomName);
    }
}
