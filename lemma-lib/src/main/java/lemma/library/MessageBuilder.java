package lemma.library;

import org.json.JSONArray;
import org.json.JSONObject;

public class MessageBuilder {
    private static NoamLogger logger = NoamLogger.instance();

    private final String lemmaID;

    private static final String dialect = "java";
    private static final String version = "1.1";

    MessageBuilder(String lemmaId) {
        this.lemmaID = lemmaId;
    }

    public static void add(JSONArray root, Object value) {
        try {
            if(value instanceof Object[]){
                Object[] values = (Object[])value;
                JSONArray result = new JSONArray();
                for (int i = 0; i < values.length; i++) {
                    if (!values[i].equals("")) {
                        result.put(values[i]);
                    }
                }
                root.put(result);
            } else {
                root.put(value);
            }
        } catch (Exception e) {
            logger.warn(MessageBuilder.class, "Couldn't add " + value.getClass() + " — " + value + " - to array : " + e);
        }
    }

    public String buildEvent(final String name, Object value) {
        JSONArray root = new JSONArray();

        add(root, "event");
        add(root, lemmaID);
        add(root, name);
        add(root, value);

        return root.toString();
    }

    public String buildRegister(int port, String[] hears, int hearsSize, String[] plays, int playsSize) {
        JSONArray root = new JSONArray();

        add(root, "register");
        add(root, lemmaID);
        add(root, port);

        add(root, hears);
        add(root, plays);

        add(root, dialect);
        add(root, version);

        return root.toString();
    }

    public String buildMarco(String roomName) {
        JSONArray root = new JSONArray();
        add(root, "marco");
        add(root, lemmaID);
        add(root, roomName);
        add(root, dialect);
        add(root, version);

        return root.toString();
    }
}