package lemma.library;

import org.json.*;

public class MessageParser {
    static Event parse( String message ){
		try {
			JSONArray messageArray = new JSONArray( message );
			String eventName = messageArray.getString(2);
			Object eventValue = messageArray.get(3);

			Event result;

			if (eventValue instanceof Integer){
				int val = (Integer)eventValue;
				result = new Event( eventName, val );
				return result;
			}
			else if (eventValue instanceof Float){
				float val = (Float)eventValue;
				result = new Event( eventName, val );
				return result;
			}
			else if (eventValue instanceof Double){
				double val = (Double)eventValue;
				result = new Event( eventName, (float)val );
				return result;
			}
			else {
				String val = eventValue.toString();
				result = new Event( eventName, val );
				return result;
			}
        }
		catch (org.json.JSONException e){
			System.out.println("Failed to parse message : " + e);
			return null;
		}
	}
}