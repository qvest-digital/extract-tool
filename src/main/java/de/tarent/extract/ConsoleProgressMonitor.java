package de.tarent.extract;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.evolvis.tartools.backgroundjobs.BackgroundJobMonitor;

public class ConsoleProgressMonitor implements BackgroundJobMonitor {

    private final PrintWriter printWriter;
    private int total;
    private int done;
    private final int barWidth;
    private int lastLength = 0;

    public ConsoleProgressMonitor(final PrintWriter printWriter, final int barWidth) {
        this.printWriter = printWriter;
        this.barWidth = barWidth;
    }

    @Override
    public void log(final Severity severity, final Object message) {
        printWriter.println(severity + ": " + message);
    }

    @Override
    public boolean isAborting() {
        return false;
    }

    @Override
    public String getScheduledJobId() {
        return "cli-job";
    }

    @Override
    public void announceTotal(final int totalItems) {
        total = totalItems;
    }

    @Override
    public void reportProgressIncrement(final int items) {
        done += items;
        printProgress();
    }

    private void printProgress() {
        // "Progress: [======..............] 204099 of 300000"
        final StringWriter sb = new StringWriter();
        sb.write("Progress: [");
        for (int i = 0; i < barWidth; i++) {
            final double doneRatio = ((double) done) / ((double) total);
            if ((i) < doneRatio * (barWidth)) {
                sb.write("=");
            }
            else {
                sb.write(".");
            }
        }
        sb.write("] ");
        sb.write("" + done);
        sb.write(" of ");
        sb.write("" + total);
        final int currentLength = sb.toString().length();
        for (int i = sb.toString().length(); i < lastLength; i++) {
            sb.append(' ');
        }

        lastLength = currentLength;
        printWriter.print(sb.toString());
        printWriter.print('\r');
        printWriter.flush();

    }

    @Override
    public void reportProgressAbsolute(final int items) {
        done = items;
        printProgress();
    }

}
