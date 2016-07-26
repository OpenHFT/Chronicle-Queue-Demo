package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.MethodReader;

/**
 * Created by catherine on 17/07/2016.
 */
public class OutputMain {
    public static void main(String[] args) throws InterruptedException {
        String path = "queue-fr";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        MessageConsumer messagePrinter = System.out::println;
        MethodReader methodReader = queue.createTailer().methodReader(messagePrinter);

        while (true) {
            if (!methodReader.readOne())
                Thread.sleep(10);
        }
    }
}
