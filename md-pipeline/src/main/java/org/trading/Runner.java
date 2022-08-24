package org.trading;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.util.function.Function;

class Runner {
    /**
     * Start up and run a microservice listening to {@code inq} and outputting to {@code outq}
     */
    static <T> void run(String inq, String outq, Class<T> mwClass, Function<T, Object> serviceVendor) {
        try (ChronicleQueue in = SingleChronicleQueueBuilder.binary(inq).build();
             ChronicleQueue out = SingleChronicleQueueBuilder.binary(outq).build()) {
            // create a Chronicle Wire method writer such that when the service calls a method on
            // the "out" method writer, the method call is serialised and written to the out queue
            T mw = out.acquireAppender().methodWriter(mwClass);
            Object service = serviceVendor.apply(mw);
            // create a method reader to read in the in queue for any and all methods implemented by the service
            MethodReader mr = in.createTailer().toEnd().methodReader(service);
            while (!Thread.currentThread().isInterrupted()) {
                // and dispatch these events to the service
                if (!mr.readOne())
                    Jvm.pause(10);
            }
        }
    }
}
