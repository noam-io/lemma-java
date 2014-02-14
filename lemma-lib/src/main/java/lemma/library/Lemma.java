package lemma.library;

import org.json.JSONObject;

import java.util.logging.Level;

public class Lemma {
    public final static String VERSION = "##library.prettyVersion##";
    public final int tcpListenPort;
    NoamLogger logger = NoamLogger.instance();
    Object parent;
    private TCPClient moderatorClient;
    private TCPServer eventServer;
    private ModeratorLocator moderatorLocator;
    private MessageSender messageSender;
    private EventFilter filter;

    public Lemma(Object parent, String lemmaID, String desiredServerName) {
        this(parent, lemmaID, desiredServerName, Level.ALL);
    }

    public Lemma(Object parent, String lemmaID, String desiredServerName, Level level) {
        this.parent = parent;
        logger.setLevel(level);
        eventServer = new TCPServer(parent, 0);
        this.tcpListenPort = eventServer.server.getLocalPort();

        moderatorLocator = new ModeratorLocator(lemmaID, desiredServerName);
        messageSender = new MessageSender(lemmaID);
        filter = new EventFilter();
    }

    public static void main(String[] args) {
        Lemma lemma = new Lemma(new Object(), "test", "Noam");
        int messagesSent = 0;
        while(true) {
            if (lemma.sendEvent("messagesSent", messagesSent)) {
                messagesSent++;
            }
            lemma.run();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

                logger.info(this.getClass(), "Attempting connection to Moderator @ " + moderatorIp + " : " + moderatorPort);
                moderatorClient = new TCPClient(parent, moderatorIp, moderatorPort);

                if (moderatorClient.active()) {
                    logger.info(this.getClass(), "Moderator Connection established");
                    messageSender.setClient(moderatorClient);
                } else {
                    moderatorLocator.reset();
                    logger.warn(this.getClass(), "Moderator Connection failed / dropped");
                }

                messageSender.sendRegistration(tcpListenPort, filter.events(), filter.count(), new String[0], 0);
            }
        }
    }

    private void handleIncomingConnections() {
        TCPClient incomingClient = eventServer.available();

        if (incomingClient != null) {
            TCPReader reader = new TCPReader(incomingClient);
            String message = reader.read();

            if (!message.equals("")) {
                Event event = MessageParser.parseEvent(message);
                filter.handle(event);
            }
        }
    }

    public void hear(String name, EventHandler callback) {
        filter.add(name, callback);
    }

    public boolean sendEvent(String name, String value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.debug(this.getClass(), "Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, int value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.debug(this.getClass(), "Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, double value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.debug(this.getClass(), "Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, float value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.debug(this.getClass(), "Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, JSONObject value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.debug(this.getClass(), "Unable to send [" + name + " : " + value + "] ... Aborting Connection");
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