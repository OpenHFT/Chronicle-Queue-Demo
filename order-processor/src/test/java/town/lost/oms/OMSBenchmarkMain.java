/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.affinity.AffinityLock;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.DocumentContext;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.AbstractEvent;
import town.lost.oms.dto.BuySell;
import town.lost.oms.dto.NewOrderSingle;
import town.lost.oms.dto.OrderType;
// isolcpus=5,6,7 set in grub.cfg
// sudo cpupower frequency-set -g performance -d 4.5g

// -Xmx64m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=name=test,filename=test.jfr,dumponexit=true,settings=profile -XX:-UseTLAB
/*
All run with -Dthroughput=100000 on a 4.5 Ghz Centos 7.5 system.
extends SelfDescribingMarshallable - default
Percentile   run1         run2         run3         run4         run5      % Variation
50:             1.01         1.03         1.06         1.12         1.15         7.32
90:             1.76         1.26         1.32         1.39         1.45         9.18
99:             2.75         2.47         2.50         2.55         2.59         3.19
99.7:           3.37         2.71         2.73         2.82         2.86         3.56
99.9:          11.36        11.31        11.31        11.42        11.49         1.07

// extends SelfDescribingMarshallable - with code generation
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.92         0.81         0.85         0.87         0.88         5.83
90:             1.09         1.07         1.07         1.22         1.23         8.85
99:             2.35         2.24         2.24         2.31         2.33         2.61
99.7:           2.64         2.46         2.41         2.51         2.52         2.85
99.9:           6.30         3.76         3.09         3.75         3.59        12.67

// extends BytesInBinaryMarshallable
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.65         0.71         0.71         0.66         0.66         5.02
90:             0.79         0.90         0.90         0.78         0.78         9.09
99:             2.05         2.01         2.01         2.03         1.98         1.59
99.7:           2.31         2.17         2.16         2.18         2.14         1.48
99.9:           3.00         2.74         2.66         2.65         2.45         7.51

// extends BytesInBinaryMarshallable - with code generation + MethodIds
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.54         0.55         0.54         0.56         0.55         2.40
90:             0.61         0.65         0.62         0.67         0.63         5.14
99:             1.95         1.88         1.91         1.90         1.89         1.09
99.7:           2.15         2.08         2.06         2.08         2.07         0.64
99.9:           2.74         2.46         2.36         2.34         2.36         3.25

// -Dthroughput=100000 on a i4770 windows laptop
Percentile   run1         run2         run3         run4         run5      % Variation
50:             0.80         0.80         0.80         0.80         0.80         0.00
90:             0.90         1.00         0.90         0.80         0.80        14.28
99:             2.90         3.10         2.70         2.70         2.70         8.99
99.7:           4.80         4.80         3.90         3.40         3.40        21.55
99.9:          19.40        14.70        14.60        14.30        14.40         1.83
worst:       1678.85     28090.37     12480.51      1005.31      6952.96        94.73
 */
public class OMSBenchmarkMain {
    public static final int THROUGHPUT = Integer.getInteger("throughput", 100_000);
    public static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;
    public static final String PATH = System.getProperty("path", OS.TMP);
    public static final boolean ACCOUNT_FOR_COORDINATED_OMMISSION = Boolean.getBoolean("accountForCoordinatedOmmission");

    static {
        System.setProperty("pauser.minProcessors", "2");
    }

    @SuppressWarnings("try")
    public static void main(String[] args) {
        printProperties();

        String tmpDir = PATH + "/bench-" + System.nanoTime();
        try (ChronicleQueue input = ChronicleQueue.single(tmpDir + "/input");
             ChronicleQueue output = ChronicleQueue.single(tmpDir + "/output")) {

            // processing thread
            Thread processor = new Thread(() -> {
                try (AffinityLock ignored = AffinityLock.acquireCore()) {
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
                }
            }, "processor");
            processor.start();

            JLBH jlbh = new JLBH(new JLBHOptions()
                    .warmUpIterations(50000)
                    .pauseAfterWarmupMS(500)
                    .throughput(THROUGHPUT)
                    .iterations(Math.min(2_000_000, THROUGHPUT * 10))
                    .runs(5)
                    .recordOSJitter(false)
                    .accountForCoordinatedOmission(ACCOUNT_FOR_COORDINATED_OMMISSION)
                    .jlbhTask(new MyJLBHTask(input)));
            jlbh.start();
            processor.interrupt();
        }
        printProperties();
        Jvm.pause(1000);
        IOTools.deleteDirWithFiles(tmpDir);
    }

    private static void printProperties() {
        long estimatedMemory = Math.round(Runtime.getRuntime().totalMemory() / 1e6);
        System.out.println("-Xmx" + estimatedMemory + "m " +
                "-DbyteInBinary=" + AbstractEvent.BYTES_IN_BINARY + " " +
                "-DpregeneratedMarshallable=" + AbstractEvent.PREGENERATED_MARSHALLABLE + " " +
                "-Dthroughput=" + THROUGHPUT + " " +
                "-Dpath=" + PATH + " " +
                "-DaccountForCoordinatedOmission=" + ACCOUNT_FOR_COORDINATED_OMMISSION);
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
