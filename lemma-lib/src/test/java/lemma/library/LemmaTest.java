//Copyright (c) 2014, IDEO 

package lemma.library;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LemmaTest {

    @Ignore
    @Test
    public void canary() {
        Lemma lemma = new Lemma(null, "Lemma Name", "Room Name");
        assertEquals("foo", lemma.tcpListenPort);
    }

    @Test
    public void testValidNames() {
        Lemma lemma = new Lemma("ExampleParent", "LemmaName", "Room Name");
        lemma = new Lemma("_ExampleParent", "LemmaName", "Room Name");
        lemma = new Lemma("_ExampleParent90", "LemmaName", "Room Name");
        lemma = new Lemma("_Example_Parent1", "LemmaName", "Room Name");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNames() {
        Lemma lemma = new Lemma(null, "Lemma Name", "Room Name");
        lemma = new Lemma(null, "1LemmaName", "Room Name");
        lemma = new Lemma(null, "LemmaName!", "Room Name");
        lemma = new Lemma(null, "LemmaName?", "Room Name");
    }
}
