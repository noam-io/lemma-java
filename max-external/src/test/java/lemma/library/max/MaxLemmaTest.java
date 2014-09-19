package lemma.library.max;

//Copyright (c) 2014, IDEO


import com.cycling74.max.Atom;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class MaxLemmaTest {
    @Test
    public void defaultToFreeAgent() {
        MaxLemma maxLemma = new MaxLemma();
        assertEquals("", maxLemma.getAttr("ROOM_NAME"));
    }

    @Ignore
    @Test
    public void specifyRoomName() {
        MaxLemma maxLemma = new MaxLemma();
        maxLemma.setAttr("ROOM_NAME", "DesiredRoom"); //why do we get a stack overflow here!?
        assertEquals("DesiredRoom", maxLemma.getAttr("ROOM_NAME"));
    }

}
