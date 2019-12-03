/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.core.jlbh.JLBH;
import net.openhft.chronicle.core.jlbh.JLBHOptions;
import net.openhft.chronicle.core.jlbh.JLBHTask;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.DocumentContext;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.BuySell;
import town.lost.oms.dto.NewOrderSingle;
import town.lost.oms.dto.OrderType;

import java.io.File;

public class OMSBenchmarkMain {

    public static final int THROUGHPUT = Integer.getInteger("throughput", 20_000);
    public static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;

    public static void main(String[] args) {
        String tmp = new File("/dev/shm").exists() ? "/dev/shm" : OS.TMP;
        String tmpDir = tmp + "/bench-" + System.nanoTime();
        try (ChronicleQueue input = ChronicleQueue.single(tmpDir + "/input");
             ChronicleQueue output = ChronicleQueue.single(tmpDir + "/output")) {

            // processing thread
            Thread processor = new Thread(() -> {
                OMSOut out = output.acquireAppender().methodWriter(OMSOut.class);
                OMSImpl oms = new OMSImpl(out);
                MethodReader in = input.createTailer("test").methodReader(oms);
                Pauser pauser = Pauser.busy();
                while (!Thread.currentThread().isInterrupted()) {
                    if (in.readOne())
                        pauser.reset();
                    else
                        pauser.pause();
                }
            }, "processor");
            processor.start();

            JLBH jlbh = new JLBH(new JLBHOptions()
                    .warmUpIterations(50000)
                    .throughput(THROUGHPUT)
                    .iterations(THROUGHPUT * 5)
                    .runs(5)
                    .recordOSJitter(false)
                    .accountForCoordinatedOmmission(false)
                    .jlbhTask(new JLBHTask() {
                        private JLBH jlbh;
                        private NewOrderSingle nos = new NewOrderSingle()
                                .sender(BASE85.parse("client"))
                                .target(BASE85.parse("OMS"))
                                .clOrdID("clOrdId")
                                .orderQty(1e6)
                                .price(1.6)
                                .symbol(BASE85.parse("AUDUSD"))
                                .ordType(OrderType.limit)
                                .side(BuySell.buy);
                        private ExcerptTailer tailer = input.createTailer();

                        @Override
                        public void init(JLBH jlbh) {
                            this.jlbh = jlbh;
                        }

                        @Override
                        public void run(long startTimeNS) {
                            try {
                                OMSIn in = input.acquireAppender().methodWriter(OMSIn.class);
                                in.newOrderSingle(nos.sendingTime(startTimeNS));
                                long start = System.currentTimeMillis();
                                while (true) {
                                    try (DocumentContext dc = tailer.readingDocument()) {
                                        if (dc.isPresent())
                                            break;
                                    }
                                    if (start + 1e3 > System.currentTimeMillis()) {
                                        System.err.println("Failed to get message");
                                        break;
                                    }
                                }

                                jlbh.sampleNanos(System.nanoTime() - startTimeNS);
/*
                                try (DocumentContext dc = tailer.readingDocument()) {
                                    if (dc.isPresent())
                                        throw new AssertionError();
                                }
*/

                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                    }));
            jlbh.start();
            processor.interrupt();
        }
        Jvm.pause(1000);
        IOTools.deleteDirWithFiles(tmpDir);
    }
}
