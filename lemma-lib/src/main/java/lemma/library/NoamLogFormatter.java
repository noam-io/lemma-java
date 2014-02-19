package lemma.library;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class NoamLogFormatter extends Formatter {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    @Override
    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();

        sb.append(new Date(record.getMillis()))
                .append(" ")
                .append(record.getLevel().getLocalizedName())
                .append(": ")
                .append(formatMessage(record))
                .append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            record.getThrown().printStackTrace(pw);
            pw.close();
            sb.append(sw.toString());
        }

        return sb.toString();
    }
}
