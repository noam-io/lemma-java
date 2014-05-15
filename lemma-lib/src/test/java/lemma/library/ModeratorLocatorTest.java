//Copyright (c) 2014, IDEO 

package lemma.library;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.logging.Level;

import static org.junit.Assert.assertEquals;

public class ModeratorLocatorTest {
    private ModeratorLocator locator;
    private Level initialLevel;

    @Before
    public void setUp() {
        locator = new ModeratorLocator("", "Some Server Name");
    }

    @Test
    public void startsWithDefaultValue() {
        locator.tryLocate();

        assertEquals("", locator.locatedIp);
        assertEquals(0, locator.locatedPort);
        assertEquals("", locator.locatedServerName);
    }

    @Test
    public void parsesAPacket() {
        locator.incomingPacket = locator.new Packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[\"polo\",\"Room Name\",1345]";
        locator.tryLocate();

        assertEquals("11.22.33.44", locator.locatedIp);
        assertEquals(1345, locator.locatedPort);
        assertEquals("Room Name", locator.locatedServerName);
    }

    @Test
    public void fallsBackToDefaultOnLegacyMessage() {
        locator.incomingPacket = locator.new Packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[Maestro@4321]";
        locator.tryLocate();

        assertEquals("", locator.locatedIp);
        assertEquals(0, locator.locatedPort);
        assertEquals("", locator.locatedServerName);
    }
}
