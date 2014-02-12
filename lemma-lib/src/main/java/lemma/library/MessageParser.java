package lemma.library;

import org.json.JSONArray;

public class MessageParser {
    private static NoamLogger logger = NoamLogger.instance();

    static Event parseEvent(String message) {
        try {
            JSONArray messageArray = new JSONArray(message);
            String eventName = messageArray.getString(2);
            Object eventValue = messageArray.get(3);

            Event result;

            if (eventValue instanceof Integer) {
                int val = (Integer) eventValue;
                result = new Event(eventName, val);
                return result;
            } else if (eventValue instanceof Float) {
                float val = (Float) eventValue;
                result = new Event(eventName, val);
                return result;
            } else if (eventValue instanceof Double) {
                double val = (Double) eventValue;
                result = new Event(eventName, (float) val);
                return result;
            } else {
                String val = eventValue.toString();
                result = new Event(eventName, val);
                return result;
            }
        } catch (org.json.JSONException e) {
            logger.warn(MessageParser.class, "Failed to parse event message : " + e);
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
            logger.warn(MessageParser.class, "Failed to parse polo message : " + e);
            return null;
        }
    }
}