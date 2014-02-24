package lemma.library;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class TCPReaderTest {

    @Test
    public void readsAGoodMessage() throws IOException {
        FakeTcpClient client = new FakeTcpClient("000009something");
        TCPReader reader = new TCPReader(client);
        String message = reader.read();
        assertEquals("something", message);
    }

    @Test
    public void readsAMessageWithSmallTcpBuffer() throws IOException {
        FakeTcpClient client = new FakeTcpClient("000009ohaithere", 4);
        TCPReader reader = new TCPReader(client);
        String message = reader.read();
        assertEquals("ohaithere", message);
    }

    @Test
    public void readsAMessageWithExtraStuffOnTheBuffer() throws IOException {
        FakeTcpClient client = new FakeTcpClient("000009ohaithere000004hehe", 4);
        TCPReader reader = new TCPReader(client);
        String message = reader.read();
        assertEquals("ohaithere", message);
    }

    @Test
    public void handlesAMalformedMessage() throws IOException {
        FakeTcpClient client = new FakeTcpClient("nolengthhere");
        TCPReader reader = new TCPReader(client);
        String message = reader.read();
        assertEquals("", message);
    }
}
