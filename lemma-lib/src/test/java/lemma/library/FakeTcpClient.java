package lemma.library;

import java.io.IOException;

public class FakeTcpClient extends TCPClient {

    private String contents = "";
    private int bufferSize;
    private int cursor = 0;

    public FakeTcpClient(String contents) throws IOException {
        this(contents, 512);
    }

    public FakeTcpClient(String contents, int bufferSize) throws IOException {
        super(new Object(), new FakeSocket());
        setContents(contents);
        setBufferSize(bufferSize);
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void run() {
    }

    @Override
    public int readBytes(byte[] copyBuffer) {
        int cap = Math.min(contents.length(), cursor + bufferSize);
        byte[] window = contents.substring(cursor, cap).getBytes();

        int bytesRead = 0;
        for (; cursor < bufferSize && bytesRead < copyBuffer.length; cursor++, bytesRead++) {
            byte contentByte = window[bytesRead];
            copyBuffer[bytesRead] = contentByte;
        }

        if (cursor == bufferSize) {
            contents = contents.substring(bufferSize);
            cursor = 0;
        }
        return bytesRead;
    }
}
