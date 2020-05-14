/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.Mocker;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import town.lost.oms.api.OMSIn;

public class OrderViewerMain {
    public static void main(String[] args) {
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("in").rollCycle(RollCycles.TEST8_DAILY).build()) {
            OMSIn logging = Mocker.logging(OMSIn.class, "read - ", System.out);
            MethodReader reader = q.createTailer().methodReader(logging);
            while (true) {
                if (!reader.readOne())
                    Jvm.pause(50);
            }
        }
    }
}
