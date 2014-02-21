package lemma.library;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NoamLogSettings {
    private static Level level = Level.INFO;

    public static Level getLevel() {
        return level;
    }

    public static void setLevel(Level newLevel) {
        level = newLevel;

        Logger rootLogger = Logger.getLogger("lemma.library");
        rootLogger.setUseParentHandlers(false);
        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }

        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(newLevel);
        handler.setFormatter(new NoamLogFormatter());

        rootLogger.addHandler(handler);

        rootLogger.setLevel(newLevel);
    }

    public static void setDefaultLevel() {
        setLevel(level);
    }
}
