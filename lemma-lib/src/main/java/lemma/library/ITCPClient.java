package lemma.library;

public interface ITCPClient {
    /**
     * Grab whatever is in the serial buffer, and stuff it into a
     * byte buffer passed in by the user. This is more memory/time
     * efficient than readBytes() returning a byte[] array.
     *
     * Returns an int for how many bytes were read. If more bytes
     * are available than can fit into the byte array, only those
     * that will fit are read.
     *
     * @param copyBuffer passed in byte array to be altered
     *
     */
    int readBytes(byte[] copyBuffer);
}
