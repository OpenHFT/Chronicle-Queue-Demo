package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.queue.DumpQueueMain;

/**
 * Created by catherine on 18/07/2016.
 */
public class PrintQueueMain {
    public static void main(String[] args) {
        DumpQueueMain.dump("queue-en");
        DumpQueueMain.dump("queue-fr");
    }
}
