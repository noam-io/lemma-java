//Copyright (c) 2014, IDEO 

package lemma.library;

import org.json.JSONObject;

import java.util.logging.Logger;

public class MessageSender {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public MessageBuilder messageBuilder;
	public TCPClient outboundClient;

    public MessageSender( String lemmaID ){
        this(lemmaID, null);
    }
    public MessageSender( String lemmaID, TCPClient outboundClient ){
        this.messageBuilder = new MessageBuilder( lemmaID );
		this.outboundClient = outboundClient;
	}
	public boolean isConnected(){
		if (outboundClient == null) return false;
		else if (!outboundClient.active()){
			stop();
			return false;
		}
		else return true;
	}
	public void stop(){
		if (outboundClient != null) outboundClient.stop();
		setClient(null);
	}
	public void setClient(TCPClient client){
		outboundClient = client;
	}
	public boolean sendRegistration( int listenPort, String[] hears, int hearsCount, String[] plays, int playsCount ){
		String registrationMessage = messageBuilder.buildRegister( listenPort, hears, hearsCount, plays, playsCount );
		boolean result = sendMessage( registrationMessage );
		return result;
	}
	public boolean sendEvent( String name, Object value ){
		String eventMessage = messageBuilder.buildEvent( name, value );
		boolean result = sendMessage( eventMessage );
		return result;
	}

    private boolean sendMessage( String message ){
		if (this.isConnected()){
			String encoded = TCPProtocol.encode( message );
			outboundClient.write(encoded); 		// Missing check here? Was it written?
			logger.fine("Sent event : " + encoded);
			return true;
		}
		else {
			return false;
		}
	}
}
