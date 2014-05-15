//Copyright (c) 2014, IDEO 

package lemma.library;

import org.json.JSONObject;

import java.util.logging.*;
import java.util.regex.Pattern;

public class Lemma {
    public final static String VERSION = "##library.prettyVersion##";
    private Logger logger = Logger.getLogger(this.getClass().getName());

    Object parent;
    public int tcpListenPort;
    private TCPClient moderatorClient;
    private TCPServer eventServer;
    private ModeratorLocator moderatorLocator;
    private MessageSender messageSender;
    private EventFilter filter;

    static {
        NoamLogSettings.setDefaultLevel();
    }

    public Lemma(Object parent, String lemmaID, String desiredServerName) {
        initialize(parent, lemmaID, desiredServerName);
    }

    public Lemma(Object parent, String lemmaID, String desiredServerName, Level level) {
        NoamLogSettings.setLevel(level);
        initialize(parent, lemmaID, desiredServerName);
    }

    public void initialize (Object parent, String lemmaID, String desiredServerName) {
        if(this.isValidLemmaId(lemmaID)){
            this.parent = parent;
            eventServer = new TCPServer(parent, 0);
            this.tcpListenPort = eventServer.server.getLocalPort();

            moderatorLocator = new ModeratorLocator(lemmaID, desiredServerName);
            messageSender = new MessageSender(lemmaID);
            filter = new EventFilter();
        } else {
            throw new IllegalArgumentException("Lemma names may only contain alphanumeric characters or underscores (_). They also may not begin with a number.");
        }
    }

    public static void main(String[] args) {
        Lemma lemma = new Lemma(new Object(), "test", "");
//        Lemma lemma = new Lemma(new Object(), "Java", "Desired Room", Level.INFO);
        int messagesSent = 0;
        while(true) {
            if (lemma.sendEvent("messagesSent", messagesSent)) {
                messagesSent++;
            }
            lemma.run();
            try {
                Thread.sleep(1000);
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

                logger.fine("Attempting connection to Moderator @ " + moderatorIp + ":" + moderatorPort);
                moderatorClient = new TCPClient(parent, moderatorIp, moderatorPort);

                if (moderatorClient.active()) {
                    logger.info("Noam is connected!");
                    messageSender.setClient(moderatorClient);
                } else {
                    moderatorLocator.reset();
                    logger.warning("Noam connection failed / dropped");
                }

                messageSender.sendRegistration(tcpListenPort, filter.events(), filter.count(), new String[0], 0);
                moderatorLocator.reset();
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
            logger.fine("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, int value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.fine("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, double value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.fine("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public boolean sendEvent(String name, JSONObject value) {
        if (!messageSender.sendEvent(name, value)) {
            logger.fine("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
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

    private boolean isValidLemmaId(String lemmaId){
        return Pattern.matches("^[a-zA-Z_][a-zA-Z0-9_]*$", lemmaId);
    }
}
