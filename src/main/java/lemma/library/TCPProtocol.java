package lemma.library;

public class TCPProtocol {
	public static String encode( String message ) {
		String result = String.format("%06d%s", message.length(), message);

		return result;
	}
}