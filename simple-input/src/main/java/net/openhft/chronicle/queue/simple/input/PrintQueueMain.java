package net.openhft.chronicle.queue.simple.input;

import net.openhft.chronicle.queue.DumpQueueMain;

import java.io.FileNotFoundException;

/**
 * Created by catherine on 18/07/2016.
 */
public class PrintQueueMain {
    public static void main(String[] args) throws FileNotFoundException {
        DumpQueueMain.dump("queue");
    }
}
