package lemma.library;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaestroLocatorTest {
    private MaestroLocator locator;

    @Before
    public void setUp() {
        locator = new MaestroLocator(1030);
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
        locator.incomingPacket = locator.new packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[Maestro@4321,'this.is-my_server']";
        locator.tryLocate();

        assertEquals("11.22.33.44", locator.ip);
        assertEquals(4321, locator.port);
        assertEquals("this.is-my_server", locator.serverName);
    }

    @Test
    public void fallsBackToDefaultOnLegacyMessage() {
        locator.incomingPacket = locator.new packet();
        locator.incomingPacket.ip = "11.22.33.44";
        locator.incomingPacket.port = 9876;
        locator.incomingPacket.message = "[Maestro@4321]";
        locator.tryLocate();

        assertEquals("", locator.ip);
        assertEquals(0, locator.port);
        assertEquals("", locator.serverName);
    }
}
