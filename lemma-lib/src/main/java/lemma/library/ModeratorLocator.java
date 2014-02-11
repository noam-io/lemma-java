package lemma.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModeratorLocator {

    public UDP client;

    class Packet {
        public String message;
        public String ip;
        public int port;
    }


    public ModeratorLocator(int port) {
        client = new UDP(this, port);
        client.broadcast(false);
        client.listen(true);
    }

    public volatile Packet incomingPacket;
    public String ip = "";
    public int port = 0;
    public String serverName = "";

    public void tryLocate() {
        this.port = 0;
        this.ip = "";
        this.serverName = "";
        parsePacket();
    }

    public boolean parsePacket() {
        Packet incomingPacket = this.incomingPacket;

        if (incomingPacket != null && incomingPacket.message != null) {
            Pattern p = Pattern.compile(String.format("\\[%s@(%s)\\]", "Maestro", "\\d+"));
            Matcher m = p.matcher(incomingPacket.message);
            boolean b = m.matches();
            if (m.matches() && lastCharacterIsBracket(incomingPacket.message)) {
                this.ip = incomingPacket.ip;
                this.port = Integer.parseInt(m.group(1));
                return true;
            }
            return false;
        }
        return false;
    }

    public void receive(byte[] data, String ip, int port) {
        Packet incomingPacket = new Packet();
        incomingPacket.message = new String(data);
        incomingPacket.ip = ip;
        incomingPacket.port = port;

        this.incomingPacket = incomingPacket;
    }

    private boolean lastCharacterIsBracket(String string) {
        int length = string.length();
        return string.charAt(length - 1) == ']';
    }

    public boolean foundModerator() {
        return !ip.equals("");
    }

    public String moderatorIp() {
        return ip;
    }

    public int moderatorPort() {
        return port;
    }

    public void reset() {
        this.incomingPacket = null;
    }

    public void close() {
        client.close();
    }
}