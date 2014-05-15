//Copyright (c) 2014, IDEO 

package lemma.library;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertEquals;

public class MessageBuilderTest {

    private MessageBuilder builder;
    private String msg;

    @Before
    public void setUp() {
        builder = new MessageBuilder("lemma_id");
    }    

    @Test
    public void buildAnEventMessage() {
        String marco = builder.buildEvent("event_name", "stuff goes here");
        assertEquals("[\"event\",\"lemma_id\",\"event_name\",\"stuff goes here\"]", marco);
        Event event = MessageParser.parseEvent(marco);
        assertEquals("event_name", event.name);
        assertEquals("stuff goes here", event.stringValue);
    }

    @Test
    public void buildAMarcoMessage() {
        String marco = builder.buildMarco("Room Name");
        assertEquals("[\"marco\",\"lemma_id\",\"Room Name\",\"java\",\"1.1\"]", marco);
    }

    @Test
    public void buildEventBool() {
        boolean value = true;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",true]", msg);

        value = false;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",false]", msg);
    }

    @Test
    public void buildEventInt() {
        int value = 10;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",10]", msg);

        value = -10;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",-10]", msg);
    }

    @Test
    public void buildEventDouble() {
        double value = 11.5;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",11.5]", msg);

        value = -12.5;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",-12.5]", msg);
    }

    @Test
    public void buildEventFloat() {
        float value = 11.5f;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",11.5]", msg);

        value = -12.5f;
        msg = builder.buildEvent("eventName", value);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",-12.5]", msg);
    }

    @Test
    public void buildEventString() {
        String blankvalue = "";
        msg = builder.buildEvent("eventName", blankvalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",\"\"]", msg);

        String escapevalue = "\"";
        msg = builder.buildEvent("eventName", escapevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",\"\\\"\"]", msg);

        String strvalue = "Here I Am";
        msg = builder.buildEvent("eventName", strvalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",\"Here I Am\"]", msg);
    }

    @Test
    public void buildEventBoolArray() {
        boolean[] novalue = {};
        msg = builder.buildEvent("eventName", novalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[]]", msg);

        boolean[] onevalue = {true};
        msg = builder.buildEvent("eventName", onevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[true]]", msg);

        boolean[] multivalue = {true, false, true};
        msg = builder.buildEvent("eventName", multivalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[true,false,true]]", msg);
    }

    @Test
    public void buildEventIntArray() {
        int[] novalue = {};
        msg = builder.buildEvent("eventName", novalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[]]", msg);

        int[] onevalue = {11};
        msg = builder.buildEvent("eventName", onevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11]]", msg);

        int[] multivalue = {11, -12, 13};
        msg = builder.buildEvent("eventName", multivalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11,-12,13]]", msg);
    }

    @Test
    public void buildEventDoubleArray() {
        double[] novalue = {};
        msg = builder.buildEvent("eventName", novalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[]]", msg);

        double[] onevalue = {11.5};
        msg = builder.buildEvent("eventName", onevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11.5]]", msg);

        double[] multivalue = {11.5, -12.5, 13.5};
        msg = builder.buildEvent("eventName", multivalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11.5,-12.5,13.5]]", msg);
    }

    @Test
    public void buildEventFloatArray() {
        float[] novalue = {};
        msg = builder.buildEvent("eventName", novalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[]]", msg);

        float[] onevalue = {11.5f};
        msg = builder.buildEvent("eventName", onevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11.5]]", msg);

        float[] multivalue = {11.5f, -12.5f, 13.5f};
        msg = builder.buildEvent("eventName", multivalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[11.5,-12.5,13.5]]", msg);
    }

    @Test
    public void buildEventStringArray() {
        String[] novalue = {};
        msg = builder.buildEvent("eventName", novalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[]]", msg);

        String[] onevalue = {"Test"};
        msg = builder.buildEvent("eventName", onevalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[\"Test\"]]", msg);

        String[] multivalue = {"Test", "Hello", "World"};
        msg = builder.buildEvent("eventName", multivalue);
        assertEquals("[\"event\",\"lemma_id\",\"eventName\",[\"Test\",\"Hello\",\"World\"]]", msg);
    }

}
