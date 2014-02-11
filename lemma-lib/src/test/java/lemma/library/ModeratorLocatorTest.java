package lemma.library;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ModeratorLocatorTest {
    private ModeratorLocator locator;

    @Before
    public void setUp() {
        locator = new ModeratorLocator(1030);
    }

    @Test
    public void startsWithDefaultValue() {
        locator.tryLocate();

        assertEquals("", locator.ip);
        assertEquals(0, locator.port);
        assertEquals("", locator.serverName);
    }

    @Test
    public void parsesAPacket() {
        locator.incomingPacket = locator.new Packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[Maestro@4321]";
        locator.tryLocate();

        assertEquals("11.22.33.44", locator.ip);
        assertEquals(4321, locator.port);
    }

    @Ignore
    @Test
    public void fallsBackToDefaultOnLegacyMessage() {
        locator.incomingPacket = locator.new Packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[Maestro@4321]";
        locator.tryLocate();

        assertEquals("", locator.ip);
        assertEquals(0, locator.port);
        assertEquals("", locator.serverName);
    }
}
