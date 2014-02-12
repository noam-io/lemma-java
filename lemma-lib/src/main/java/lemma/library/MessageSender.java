package lemma.library;

import org.json.JSONObject;

public class MessageSender {
    private static NoamLogger logger = NoamLogger.instance();

	public MessageBuilder messageBuilder;
	public TCPClient outboundClient;

	public MessageSender( String lemmaID, TCPClient outboundClient ){
		this.messageBuilder = new MessageBuilder( lemmaID );
		this.outboundClient = outboundClient;
	}
	public MessageSender( String lemmaID ){
		this.messageBuilder = new MessageBuilder( lemmaID );
		this.outboundClient = null;
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
	public boolean sendEvent( String name, String value ){
		String eventMessage = messageBuilder.buildEvent( name, value );
		boolean result = sendMessage( eventMessage );
		return result;
	}
	public boolean sendEvent( String name, int value ){
		String eventMessage = messageBuilder.buildEvent( name, value );
		boolean result = sendMessage( eventMessage );
		return result;
	}
	public boolean sendEvent( String name, double value ){
		String eventMessage = messageBuilder.buildEvent( name, value );
		boolean result = sendMessage( eventMessage );
		return result;
	}
    public boolean sendEvent( String name, JSONObject value ){
        String eventMessage = messageBuilder.buildEvent( name, value );
        boolean result = sendMessage( eventMessage );
        return result;
    }

    private boolean sendMessage( String message ){
		if (this.isConnected()){
			String encoded = TCPProtocol.encode( message );
			outboundClient.write(encoded); 		// Missing check here? Was it written?
			logger.debug(this.getClass(), "Sent event : " + encoded);
			return true;
		}
		else {
			return false;
		}
	}
}