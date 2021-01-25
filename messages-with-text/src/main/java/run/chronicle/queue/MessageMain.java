package run.chronicle.queue;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.queue.ChronicleQueue;

// run with -Xmx128m -XX:NewSize=96m -verbose:gc
public class MessageMain {
    private static final int COUNT = Integer.getInteger("count", 10_000_000);

    public static void main(String[] args) {
        for (int t = 0; t < 5; t++) {
            long start = System.nanoTime();
            String test = "test-" + start;
            try (ChronicleQueue queue = ChronicleQueue.single(test)) {
                Messages messages = queue.methodWriter(Messages.class);
                Message message = new Message();
                for (int i = 0; i < COUNT; i++) {
                    message.getText().clear().append("Hello ").append(i);
                    message.setTimeStamp(System.currentTimeMillis());
                    messages.mesg(message);
                }

                int[] count = {0};
                Messages messagesCounter = message1 -> count[0]++;
                MethodReader reader = queue.createTailer().methodReader(messagesCounter);
                while (reader.readOne()) {
                }
                long time = System.nanoTime() - start;
                System.out.printf("Read %,d of %,d messages in %.3f seconds%n", count[0], COUNT, time / 1e9);
            }
            IOTools.deleteDirWithFiles(test);
        }
    }
}
