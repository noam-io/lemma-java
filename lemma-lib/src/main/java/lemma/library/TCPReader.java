//Copyright (c) 2014, IDEO 

package lemma.library;

public class TCPReader {

    private ITCPClient client;

    public TCPReader(ITCPClient client) {
        this.client = client;
    }

    public String read() {
        int length = readPayloadLength();
        return readPayload(length);
    }

    private int readPayloadLength() {
        String lengthBuffer = readBlock(6);
        int length;

        try {
            length = Integer.parseInt(lengthBuffer);
        } catch (NumberFormatException e) {
            length = 0;
        }

        return length;
    }

    private String readPayload(int payloadLength) {
        return readBlock(payloadLength);
    }

    private String readBlock(int length) {
        byte result[] = new byte[length];
        byte copyBuffer[];

        int bytesRead = 0;

        while (bytesRead < length) {
            copyBuffer = new byte[length - bytesRead]; // we only want to read what we have left to go
            int lastBytesRead = bytesRead;
            int thisBytesRead = client.readBytes(copyBuffer);
            bytesRead += thisBytesRead;
            System.arraycopy(copyBuffer, 0, result, lastBytesRead, thisBytesRead);
        }

        return new String(result);
    }
}
