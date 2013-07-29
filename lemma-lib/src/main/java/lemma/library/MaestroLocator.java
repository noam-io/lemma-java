package lemma.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MaestroLocator{

	public UDP client;

	class packet {
		public String message;
		public String ip;
		public int port;
	}

	public packet incomingPacket;
	public String ip = "";
	public int port = 0;

	public MaestroLocator(int port){
		client = new UDP(this, port);
		client.broadcast(false);
		client.listen(true);
	}

	public void tryLocate(){
		this.port = 0;
		this.ip = "";

		parsePacket();
	}
	public boolean parsePacket(){
		if (incomingPacket != null && incomingPacket.message != null) {
            Pattern p = Pattern.compile("\\[Maestro@(\\d+)\\]");
            Matcher m = p.matcher(incomingPacket.message);
            boolean b = m.matches();
			if( m.matches() && lastCharacterIsBracket( incomingPacket.message )){
				this.ip = incomingPacket.ip;
				this.port = Integer.parseInt(m.group(1));
				return true;
			}
			return false;
		}
		return false;
	}
	public void receive( byte[] data, String ip, int port ) {
		this.incomingPacket = new packet();
		this.incomingPacket.message = new String(data);
		this.incomingPacket.ip = ip;
		this.incomingPacket.port = port;
	}
	private boolean lastCharacterIsBracket( String string ){
		int length = string.length();
		return string.charAt(length - 1) == ']';
	}

	public boolean foundMaestro(){
		return !ip.equals("");
	}
	public String maestroIp(){
		return ip;
	}
	public int maestroPort(){
		return port;
	}
	public void reset(){
		this.incomingPacket = null;
	}
	public void close(){
		client.close();
	}
}