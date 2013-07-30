package lemma.library;

import org.json.*;

public class Lemma {
	public final static String VERSION = "##library.prettyVersion##";

	Object parent;

	private static int LISTEN_PORT = 8833;
	private static int BROADCAST_PORT;

	private static String id;

	private TCPClient maestroConnection; 	// initialized on maestro pickup

	private TCPServer maestroReceiver;
	private	MaestroLocator maestroLocator;
	private	MessageSender messageSender;
	private	EventFilter filter;

	public Lemma( Object parent, String lemmaID, int port ){
		this(parent, lemmaID, port, LISTEN_PORT);
	}

    public Lemma( Object parent, String lemmaID, int port, int listenPort ){
        this.parent = parent;

        id = lemmaID;
        BROADCAST_PORT = port;
        LISTEN_PORT = listenPort;

        maestroReceiver = new TCPServer( parent, LISTEN_PORT );
        maestroLocator = new MaestroLocator(BROADCAST_PORT);
        messageSender = new MessageSender(id);
        filter = new EventFilter();
    }

	public void run(){
        tryConnectingWithMaestro();
		handleIncomingConnections();
	}
	private void tryConnectingWithMaestro(){
		if (!messageSender.isConnected()){ 									// If we don't have a TCP connection
			messageSender.stop();              								// Reset the sender

			maestroLocator.tryLocate();
			if (maestroLocator.foundMaestro()){  							// If not already located
				String maestroIp = maestroLocator.maestroIp();     		// Save Maestro Parameters
				int maestroPort = maestroLocator.maestroPort();

				System.out.println("Attempting connection to Maestro @ " + maestroIp + " : " + maestroPort);
				maestroConnection = new TCPClient( parent, maestroIp, maestroPort );

				if (maestroConnection.active()){
					System.out.println("Maestro Connection established");
					messageSender.setClient(maestroConnection);
				}
				else{
					maestroLocator.reset();
					System.out.println("Maestro Connection failed / dropped");
				}

				messageSender.sendRegistration( LISTEN_PORT, filter.events(), filter.count(), new String[0], 0 );
			}
		}
	}
	private void handleIncomingConnections(){
		TCPClient incomingClient = maestroReceiver.available();

		if (incomingClient != null) {
			TCPReader reader = new TCPReader( incomingClient );
			String message = reader.read();

			if (message != "") {
				Event event = MessageParser.parse( message );
				filter.handle( event );
			}
		}
	}

	public void hear( String name, EventHandler callback ){
		filter.add(name, callback);
	}
	public boolean sendEvent( String name, String value ){
		if (!messageSender.sendEvent( name, value )){
			System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
			messageSender.stop();
			return false;
		}
		return true;
	}
	public boolean sendEvent( String name, int value ){
		if (!messageSender.sendEvent( name, value )){
			System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
			messageSender.stop();
			return false;
		}
		return true;
	}
	public boolean sendEvent( String name, double value ){
		if (!messageSender.sendEvent( name, value )){
			System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
			messageSender.stop();
			return false;
		}
		return true;
	}
    public boolean sendEvent( String name, JSONObject value ){
        if (!messageSender.sendEvent( name, value )){
            System.out.println("Unable to send [" + name + " : " + value + "] ... Aborting Connection");
            messageSender.stop();
            return false;
        }
        return true;
    }

    public void stop() {
        maestroLocator.close();
        maestroReceiver.stop();
        maestroConnection.stop();
    }

    public boolean connected() {
        return messageSender.isConnected();
    }
}