package lemma.library;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    @Test
    public void buildAnEventMessage() {
        MessageBuilder builder = new MessageBuilder("lemma_id");
        String marco = builder.buildEvent("event_name", "stuff goes here");
        assertEquals("[\"event\",\"lemma_id\",\"event_name\",\"stuff goes here\"]", marco);
        Event event = MessageParser.parseEvent(marco);
        assertEquals("event_name", event.name);
        assertEquals("stuff goes here", event.stringValue);
    }

    @Test
    public void buildAMarcoMessage() {
        MessageBuilder builder = new MessageBuilder("lemma_id");
        String marco = builder.buildMarco("Room Name");
        assertEquals("[\"marco\",\"lemma_id\",\"Room Name\",\"java\",\"1.1\"]", marco);
    }

}
