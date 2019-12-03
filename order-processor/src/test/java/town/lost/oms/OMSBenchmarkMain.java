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

// -Xmx64m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=name=test,filename=test.jfr,dumponexit=true,settings=profile -XX:-UseTLAB
/* extends AbstractMarshallable - default
-------------------------------- SUMMARY (end to end) -----------------------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50:             1.06         1.11         1.09         1.09         1.06         3.05
90:             1.38         1.41         1.37         1.46         1.46         4.10
99:             2.79         2.80         2.58         2.58         2.56         6.03
99.7:           3.46         3.34         2.93         2.83         2.82        10.82
99.9:           8.94         4.28         4.60        11.39        11.35        52.56

// extends AbstractMarshallable - with code generation
-------------------------------- SUMMARY (end to end) -----------------------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.80         0.85         0.88         0.88         0.90         3.85
90:             1.12         1.09         1.14         1.18         1.22         7.81
99:             2.25         2.26         2.27         2.29         2.32         1.80
99.7:           2.58         2.48         2.47         2.48         2.57         2.63
99.9:           4.04         3.62         3.47         3.51         4.03         9.75

// extends AbstractBytesMarshallable
-------------------------------- SUMMARY (end to end) -----------------------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.56         0.62         0.62         0.56         0.57         6.35
90:             0.69         0.83         0.84         0.69         0.70        12.67
99:             1.93         1.89         1.89         1.91         1.90         0.87
99.7:           2.15         2.04         2.03         2.07         2.05         1.15
99.9:           2.71         2.42         2.39         2.33         2.31         2.86

 */
public class OMSBenchmarkMain {

    public static final int THROUGHPUT = Integer.getInteger("throughput", 100_000);
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
                    .jlbhTask(new MyJLBHTask(input)));
            jlbh.start();
            processor.interrupt();
        }
        Jvm.pause(1000);
        IOTools.deleteDirWithFiles(tmpDir);
    }

    private static class MyJLBHTask implements JLBHTask {
        private JLBH jlbh;
        private NewOrderSingle nos;
        private ExcerptTailer tailer;
        private OMSIn in;

        public MyJLBHTask(ChronicleQueue input) {
            nos = new NewOrderSingle()
                    .sender(BASE85.parse("client"))
                    .target(BASE85.parse("OMS"))
                    .clOrdID("clOrdId")
                    .orderQty(1e6)
                    .price(1.6)
                    .symbol(BASE85.parse("AUDUSD"))
                    .ordType(OrderType.limit)
                    .side(BuySell.buy);
            tailer = input.createTailer();
            in = input.acquireAppender().methodWriter(OMSIn.class);
        }

        @Override
        public void init(JLBH jlbh) {
            this.jlbh = jlbh;
        }

        @Override
        public void run(long startTimeNS) {
            try {
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
    }
}
