package lemma.library;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public class ModeratorLocator {
    private static NoamLogger logger = NoamLogger.instance();
    private static final int MARCO_PORT = 1030;
    private static final int MARCO_DELAY_MILLIS = 1000;
    private final String marcoMessage;
    public UDP udp;
    public volatile Packet incomingPacket;
    public String locatedIp = "";
    public int locatedPort = 0;
    public String locatedServerName = "";
    private volatile long lastLocationAttempt = new Date().getTime();

    public ModeratorLocator(String lemmaID, String desiredServerName) {
        MessageBuilder builder = new MessageBuilder(lemmaID);
        marcoMessage = builder.buildMarco(desiredServerName);

        udp = new UDP(this);
        udp.log(true);
        udp.broadcast(true);
        udp.listen(true);
    }

    private List<String> broadcastAddresses() {
        List<String> addresses = new LinkedList<String>();

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    addresses.add(broadcast.getHostAddress());
                }
            }
        } catch (SocketException ignored) {
        }
        return addresses;
    }

    public void tryLocate() {
        this.locatedPort = 0;
        this.locatedIp = "";
        this.locatedServerName = "";

        long currentTime = new Date().getTime();
        if (currentTime - lastLocationAttempt > MARCO_DELAY_MILLIS) {

            for (String address : broadcastAddresses()) {
                udp.send(marcoMessage, address, MARCO_PORT);
            }

            lastLocationAttempt = currentTime;
        }

        parsePacket();
    }

    public void parsePacket() {
        Packet incomingPacket = this.incomingPacket;

        if (incomingPacket != null && incomingPacket.message != null) {

            logger.info(this.getClass(), "incomingPacket: " + incomingPacket.message);

            PoloMessage polo = MessageParser.parsePolo(incomingPacket.message);
            if (polo != null) {
                this.locatedIp = incomingPacket.ip;
                this.locatedPort = polo.portNumber;
                this.locatedServerName = polo.roomName;
            }
        }
    }

    public void receive(byte[] data, String ip, int port) {
        logger.info(this.getClass(), "got UDP response on polo port: " + new String(data));
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
        return !locatedIp.equals("");
    }

    public String moderatorIp() {
        return locatedIp;
    }

    public int moderatorPort() {
        return locatedPort;
    }

    public void reset() {
        this.incomingPacket = null;
    }

    public void close() {
        udp.close();
    }

    class Packet {
        public String message;
        public String ip;
        public int port;
    }
}