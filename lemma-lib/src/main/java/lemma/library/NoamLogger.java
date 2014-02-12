package lemma.library;

import java.util.logging.*;

public class NoamLogger {
    private static final NoamLogger instance = new NoamLogger();
    private Handler stdoutHandler = makeStdoutHandler();
    private Level level = Level.ALL;
    private boolean stdout = true;

    private NoamLogger() {
    }

    public static NoamLogger instance() {
        return instance;
    }

    public Handler makeStdoutHandler() {
        SimpleFormatter fmt = new SimpleFormatter();
        return new StreamHandler(System.out, fmt);
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setStdout(boolean on) {
        this.stdout = on;
    }

    private Logger getLogger(String className) {
        Logger logger = Logger.getLogger(className);
        logger.setLevel(level);
        logger.removeHandler(stdoutHandler);

        if (stdout) {
            logger.addHandler(stdoutHandler);
        }

        return logger;
    }

    public void debug(Class c, String message) {
        getLogger(c.getName()).fine(message);
    }

    public void info(Class c, String message) {
        getLogger(c.getName()).info(message);
    }

    public void warn(Class c, String message) {
        getLogger(c.getName()).warning(message);
    }

    public void error(Class c, String message) {
        getLogger(c.getName()).severe(message);
    }
}
