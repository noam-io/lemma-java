package lemma.library;

import org.json.JSONObject;

public class Lemma {
    public final static String VERSION = "##library.prettyVersion##";
    private final int listenPort;
    private final int broadcastPort;
    private final String id;
    Object parent;
    private TCPClient moderatorClient;
    private TCPServer eventServer;
    private ModeratorLocator moderatorLocator;
    private MessageSender messageSender;
    private EventFilter filter;

    public Lemma(Object parent, String lemmaID, int broadcastPort) {
        this(parent, lemmaID, broadcastPort, 8833);
    }

    public Lemma(Object parent, String lemmaID, int broadcastPort, int listenPort) {
        this.parent = parent;

        this.id = lemmaID;
        this.broadcastPort = broadcastPort;
        this.listenPort = listenPort;

        eventServer = new TCPServer(parent, this.listenPort);
        moderatorLocator = new ModeratorLocator(this.broadcastPort);
        messageSender = new MessageSender(id);
        filter = new EventFilter();
    }

    public void run() {
        tryConnectingToModerator();
        handleIncomingConnections();
    }

    private void tryConnectingToModerator() {
        if (!messageSender.isConnected()) {
            messageSender.stop();

            moderatorLocator.tryLocate();
            if (moderatorLocator.foundModerator()) {
                String moderatorIp = moderatorLocator.moderatorIp();
                int moderatorPort = moderatorLocator.moderatorPort();

                System.out.println("Attempting connection to Moderator @ " + moderatorIp + " : " + moderatorPort);
                moderatorClient = new TCPClient(parent, moderatorIp, moderatorPort);

                if (moderatorClient.active()) {
                    System.out.println("Moderator Connection established");
                    messageSender.setClient(moderatorClient);
                } else {
                    moderatorLocator.reset();
                    System.out.println("Moderator Connection failed / dropped");
                }

                messageSender.sendRegistration(listenPort, filter.events(), filter.count(), new String[0], 0);
            }
        }
    }

    private void handleIncomingConnections() {
        TCPClient incomingClient = eventServer.available();

        if (incomingClient != null) {
            TCPReader reader = new TCPReader(incomingClient);
            String message = reader.read();

            if (!message.equals("")) {
                Event event = MessageParser.parse(message);
                filter.handle(event);
            }
        }
    }

    public void hear(String name, EventHandler callback) {
        filter.add(name, callback);
    }

    public boolean sendEvent(String name, String value) {
        if (!messageSender.sendEvent(name, value)) {
            System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, int value) {
        if (!messageSender.sendEvent(name, value)) {
            System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, double value) {
        if (!messageSender.sendEvent(name, value)) {
            System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, JSONObject value) {
        if (!messageSender.sendEvent(name, value)) {
            System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public void stop() {
        moderatorLocator.close();
        eventServer.stop();
        moderatorClient.stop();
    }

    public boolean connected() {
        return messageSender.isConnected();
    }
}