package lemma.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaestroLocator {

    public UDP client;

    class packet {
        public String message;
        public String ip;
        public int port;
    }


    public MaestroLocator(int port) {
        client = new UDP(this, port);
        client.broadcast(false);
        client.listen(true);
    }

    public packet incomingPacket;
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
        if (incomingPacket != null && incomingPacket.message != null) {
            System.out.println(incomingPacket.message);
            String serverNamePattern = "[\\w\\s.\\-]+";
            Pattern p = Pattern.compile(String.format("\\[%s@(%s),'(%s)'\\]",
                                                      "Maestro",
                                                      "\\d+",
                                                      serverNamePattern));
            Matcher m = p.matcher(incomingPacket.message);
            boolean b = m.matches();
            if (m.matches() && lastCharacterIsBracket(incomingPacket.message)) {
                this.ip = incomingPacket.ip;
                this.port = Integer.parseInt(m.group(1));
                this.serverName = m.group(2);
                return true;
            }
            return false;
        }
        return false;
    }

    public void receive(byte[] data, String ip, int port) {
        this.incomingPacket = new packet();
        this.incomingPacket.message = new String(data);
        this.incomingPacket.ip = ip;
        this.incomingPacket.port = port;
    }

    private boolean lastCharacterIsBracket(String string) {
        int length = string.length();
        return string.charAt(length - 1) == ']';
    }

    public boolean foundMaestro() {
        return !ip.equals("");
    }

    public String maestroIp() {
        return ip;
    }

    public int maestroPort() {
        return port;
    }

    public void reset() {
        this.incomingPacket = null;
    }

    public void close() {
        client.close();
    }
}