package lemma.library;

import org.json.*;

public class MessageBuilder {
	private String lemmaID;

	MessageBuilder( String lemmaId ){
		this.lemmaID = lemmaId;
	}

	static public void add(JSONArray root, String value){
		try {
			root.put(value);
		}
		catch(Exception e){
			System.out.println("Couldn't add string — " + value + " - to array : " + e);
		}
	}
	static public void add(JSONArray root, int value){
		try {
			root.put(value);
		}
		catch(Exception e){
			System.out.println("Couldn't add int — " + value + " - to array : " + e);
		}
	}
	static public void add(JSONArray root, double value){
		try {
			root.put(value);
		}
		catch(Exception e){
			System.out.println("Couldn't add double — " + value + " - to array : " + e);
		}
	}
    static public void add(JSONArray root, JSONObject value){
        try {
            root.put(value);
        }
        catch(Exception e){
            System.out.println("Couldn't add JSONObject — " + value + " - to array : " + e);
        }
    }

    static public void add(JSONArray root, String[] values) {
		try {
			JSONArray result = new JSONArray();
			for (int i = 0; i < values.length; i++){
				if (!values[i].equals("")){
					result.put( values[i] );
				}
			}
			root.put(result);
		}
		catch(Exception e){
			System.out.println("Couldn't add array value — " + values[0] + " - to array : " + e);
		}
	}

	public final String buildEvent( final String name, final String value ){
		JSONArray root = new JSONArray();

		add( root, "event" );
		add( root, lemmaID );
		add( root, name );
		add( root, value );

		return root.toString();
	}
	public final String buildEvent( final String name, int value ){
		JSONArray root = new JSONArray();

		add( root, "event" );
		add( root, lemmaID );
		add( root, name );
		add( root, value );

		return root.toString();
	}
	public final String buildEvent( final String name, double value ){
		JSONArray root = new JSONArray();

		add( root, "event" );
		add( root, lemmaID );
		add( root, name );
		add( root, value );

		return root.toString();
	}

    public final String buildEvent( final String name, JSONObject value ){
        JSONArray root = new JSONArray();

        add( root, "event" );
        add( root, lemmaID );
        add( root, name );
        add( root, value );

        return root.toString();
    }



    public final String buildRegister( int port, String[] hears, int hearsSize, String[] plays, int playsSize ){
		JSONArray root = new JSONArray();

		add( root, "register" );
		add( root, lemmaID );
		add( root, port);

		add( root, hears );
		add( root, plays );

		add( root, "java");
		add( root, "1.0");

		return root.toString();
	}
}