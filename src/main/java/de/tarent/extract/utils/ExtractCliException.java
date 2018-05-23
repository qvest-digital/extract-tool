package de.tarent.extract.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ExtractCliException extends Exception {
    private static final long serialVersionUID = 3654373558875728264L;

    private final String usage;

    public ExtractCliException(final Options options, final ParseException e) {
        super(e);
        usage = usage(options);
    }

    public ExtractCliException(final Options options, final String message) {
        super(message);
        usage = usage(options);
    }

    public String getUsage() {
        return usage;
    }

    private static String usage(final Options options) {
        final StringWriter out = new StringWriter();
        new HelpFormatter().printUsage(new PrintWriter(out), 80, "extract",
                options);
        return out.toString();
    }
}
