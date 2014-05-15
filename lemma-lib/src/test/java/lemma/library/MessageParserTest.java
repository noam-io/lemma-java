//Copyright (c) 2014, IDEO 

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

    @Test
    public void parseBadPoloMessage() {
        PoloMessage polo = MessageParser.parsePolo("[\"server_response\",\"Server\", 1234]");
        assertEquals(null, polo);
    }

    @Test
    public void parseEventString() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",\"Hello World\"]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals("Hello World", event.stringValue);
    }

    @Test
    public void parseEventBool() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",true]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(true, event.booleanValue);
    }

    @Test
    public void parseEventInt() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",11]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(11, event.intValue);
    }

    @Test
    public void parseEventDouble() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",11.5]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(11.5, event.doubleValue, 0.00001);
    }

    @Test
    public void parseEventFloat() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",11.5]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(11.5f, event.floatValue, 0.00001);
    }

    @Test
    public void parseEventStringArray() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",[\"Hello\",\"Universe\",\"Test\"]]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(3, event.arraySize);
        assertEquals("Hello", event.stringArray[0]);
        assertEquals("Universe", event.stringArray[1]);
        assertEquals("Test", event.stringArray[2]);
    }

    @Test
    public void parseEventBooleanArray() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",[true, false, true]]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(3, event.arraySize);
        assertEquals(true, event.booleanArray[0]);
        assertEquals(false, event.booleanArray[1]);
        assertEquals(true, event.booleanArray[2]);
    }

    @Test
    public void parseEventIntArray() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",[1, 2, 3]]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(3, event.arraySize);
        assertEquals(1, event.intArray[0]);
        assertEquals(2, event.intArray[1]);
        assertEquals(3, event.intArray[2]);
    }

    @Test
    public void parseEventDoubleArray() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",[1.5, 2.5, 3.5]]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(3, event.arraySize);
        assertEquals(1.5, event.doubleArray[0], 0.0001);
        assertEquals(2.5, event.doubleArray[1], 0.0001);
        assertEquals(3.5, event.doubleArray[2], 0.0001);
    }

    @Test
    public void parseEventFloatArray() {
    	String msg = "[\"event\",\"lemma_id\",\"eventName\",[1.5, 2.5, 3.5]]";
        Event event = MessageParser.parseEvent(msg);
        assertEquals("eventName", event.name);
        assertEquals(3, event.arraySize);
        assertEquals(1.5, event.floatArray[0], 0.0001);
        assertEquals(2.5, event.floatArray[1], 0.0001);
        assertEquals(3.5, event.floatArray[2], 0.0001);
    }


}
