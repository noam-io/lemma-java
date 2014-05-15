//Copyright (c) 2014, IDEO 

package lemma.library;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FakeSocket extends Socket {
    public boolean closed;
    public ByteArrayInputStream input;
    public ByteArrayOutputStream output;

    public FakeSocket() {
        this.input = new ByteArrayInputStream("".getBytes());
        this.output = new ByteArrayOutputStream();
    }

    @Override
    public InputStream getInputStream() {
        this.input.reset();
        return this.input;
    }

    @Override
    public OutputStream getOutputStream() {
        output = new ByteArrayOutputStream();
        return output;
    }

    @Override
    public void shutdownOutput() {
    }

    @Override
    public synchronized void close() {
        closed = true;
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
