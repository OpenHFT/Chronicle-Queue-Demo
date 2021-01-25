package run.chronicle.queue;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.queue.ChronicleQueue;

// run with -Xmx128m -XX:NewSize=96m -verbose:gc
public class MessageMain {
    private static final int COUNT = Integer.getInteger("count", 10_000_000);

    public static void main(String[] args) {
        for (int t = 0; t < 100; t++) {
            String test = "test-" + System.nanoTime();
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
                System.out.println("Read " + count[0] + " of " + COUNT + " messages");
            }
            IOTools.deleteDirWithFiles(test);
        }
    }
}
