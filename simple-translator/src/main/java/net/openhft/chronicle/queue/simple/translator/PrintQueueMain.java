package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.queue.main.DumpMain;

import java.io.FileNotFoundException;

/**
 * Created by catherine on 18/07/2016.
 */
public class PrintQueueMain {
    public static void main(String[] args) throws FileNotFoundException {
        DumpMain.dump("queue-en");
        DumpMain.dump("queue-fr");
    }
}
