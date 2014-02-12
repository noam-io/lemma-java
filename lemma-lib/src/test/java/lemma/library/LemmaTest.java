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
}
