package lemma.library;

import org.json.JSONArray;

import java.util.logging.Logger;

public class MessageParser {
    private static Logger logger = Logger.getLogger(MessageParser.class.getName());

    static Event parseEvent(String message) {
        try {
            JSONArray messageArray = new JSONArray(message);
            String eventName = messageArray.getString(2);

            Event result;
            try {
                JSONArray subArray = messageArray.getJSONArray(3);
                Object[] eventValues = new Object[subArray.length()];
                for(int i = 0; i < subArray.length(); i++){
                    eventValues[i] = subArray.get(i);
                }
                result = new Event(eventName, eventValues);
            } catch(org.json.JSONException e){
                Object eventValue = messageArray.get(3);
                result = new Event(eventName, eventValue);
            }
            
            return result;
        } catch (org.json.JSONException e) {
            logger.warning("Failed to parse event message : " + e);
            return null;
        }
    }

    public static PoloMessage parsePolo(String message) {
        try {
            JSONArray messageArray = new JSONArray(message);
            String roomName = messageArray.getString(1);
            int portNumber = messageArray.getInt(2);
            return new PoloMessage(portNumber, roomName);
        } catch (org.json.JSONException e) {
            logger.warning("Failed to parse polo message : " + e);
            return null;
        }
    }
}