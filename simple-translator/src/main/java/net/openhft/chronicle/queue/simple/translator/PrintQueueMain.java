package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.queue.main.DumpMain;

import java.io.FileNotFoundException;

/**
 * A Main class that prints out the contents of two queues ("queue-en" and "queue-fr").
 * It uses the DumpMain utility from the Chronicle Queue library to perform the printing.
 *
 * Created by catherine on 18/07/2016.
 */
public class PrintQueueMain {
    public static void main(String[] args) throws FileNotFoundException {
        // Print the contents of the English queue
        DumpMain.dump("queue-en");

        // Print the contents of the French queue
        DumpMain.dump("queue-fr");
    }
}

