package lemma.library;

public class TCPReader {

	private TCPClient client;

	public TCPReader(TCPClient client){
		this.client = client;
	}
	public String read(){
		int length = readPayloadLength();     	// first scrape off length
		return readPayload( length );          	// then go in got payload
	}
	private int readPayloadLength(){
		String lengthBuffer = readBlock( 6 );	// read the first 6 characters into lengthbuffer

		int length = Integer.parseInt( lengthBuffer );    	// convert to int
		return length;										// returns payload length as embedded in message
	}
	private String readPayload( int payloadLength ){
		String payload = readBlock( payloadLength );

		return payload;
	}
	private String readBlock( int length ){
		byte result[] = new byte[length];
		byte copyBuffer[] = new byte[length];

		int bytesRead = 0;

		while( bytesRead < length ) {
			int lastBytesRead = bytesRead;
			int thisBytesRead = client.readBytes(copyBuffer);
			thisBytesRead = Math.min(thisBytesRead, length - lastBytesRead);
			bytesRead +=  thisBytesRead;

			for (int i = 0; i < thisBytesRead; i++){
				result[lastBytesRead + i] = copyBuffer[i];
			}
		}

		String resultString = new String(result);
		return resultString;
	}
}