package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * Created by catherine on 26/07/2016.
 */
public class TranslatorMain {
    public static void main(String[] args) throws InterruptedException {
        String pathfr = "queue-fr";
        SingleChronicleQueue queuefr = SingleChronicleQueueBuilder.binary(pathfr).build();
        MessageConsumer messageConsumer = queuefr.acquireAppender().methodWriter(MessageConsumer.class);

        MessageConsumer simpleTranslator = new SimpleTranslator(messageConsumer);

        String path_en = "queue-en";
        SingleChronicleQueue queue_en = SingleChronicleQueueBuilder.binary(path_en).build();
        MethodReader methodReader = queue_en.createTailer().methodReader(simpleTranslator);

        while (true) {
            if (!methodReader.readOne())
                Thread.sleep(10);
        }
    }
}
