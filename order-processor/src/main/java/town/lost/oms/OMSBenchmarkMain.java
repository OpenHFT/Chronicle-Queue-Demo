/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */

package town.lost.oms;

import net.openhft.affinity.AffinityLock;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.BackgroundResourceReleaser;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.threads.DiskSpaceMonitor;
import net.openhft.chronicle.wire.ShortTextLongConverter;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.*;

import static town.lost.oms.OrderAdderMain.now;
// isolcpus=5,6,7 set in grub.cfg
// sudo cpupower frequency-set -g performance -d 4.5g

// -Xmx64m -XX:+UnlockCommercialFeatures -XX:+FlightRecorder -XX:StartFlightRecording=name=test,filename=test.jfr,dumponexit=true,settings=profile -XX:-UseTLAB
/* Run on a Ryzen 9 5950X, Ubuntu 20.10
-Xmx1g -DbyteInBinary=true -DpregeneratedMarshallable=true -Dthroughput=100000 -DrunTime=120 -Dpath=/tmp
-------------------------------- SUMMARY (end to end) us -------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50.0:            2.06         1.99         1.99         1.99         1.99         0.00
90.0:            2.13         2.04         2.04         2.05         2.05         0.33
99.0:            3.16         3.09         3.09         3.10         3.10         0.17
99.7:            3.66         3.57         3.58         3.58         3.57         0.15
99.9:            4.02         3.92         3.90         3.90         3.90         0.41
99.97:           4.78         4.66         4.65         4.70         4.65         0.68
99.99:           5.58         5.29         5.21         5.34         5.14         2.43
99.997:          8.56         6.01         5.88         6.34         5.90         5.00
99.999:          9.30         6.81         6.42         9.39         6.47        23.55
worst:          50.11        74.11        10.58        13.30        11.15        80.02
----------------------------------------------------------------------------------------------------

-Xmx772m -DbyteInBinary=true -DpregeneratedMarshallable=true -Dthroughput=100000 -DrunTime=120 -Dpath=/nvme/tmp
-------------------------------- SUMMARY (end to end) us -------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50.0:            1.98         1.97         1.97         1.97         1.97         0.00
90.0:            2.05         2.04         2.04         2.04         2.04         0.00
99.0:            3.38         3.39         3.36         3.34         3.34         0.95
99.7:            6.34         6.38         5.80         5.45         5.46        10.20
99.9:           16.27        16.42        16.48        16.42        16.48         0.26
99.97:          17.38        17.38        17.38        17.25        17.38         0.49
99.99:          18.91        18.46        18.66        18.21        18.66         1.61
99.997:         24.29        22.24        22.82        21.09        25.12        11.31
99.999:         29.15        27.94        26.40        25.76       402.94        90.71
worst:         214.27        51.78       537.60        50.24      1255.42        94.12
----------------------------------------------------------------------------------------------------
 */
public class OMSBenchmarkMain {
    static {
        System.setProperty("pauser.minProcessors", "2");
        System.setProperty("disableValidate", "true");
    }

    public static final int THROUGHPUT = Integer.getInteger("throughput", 100_000);
    public static final int RUN_TIME = Integer.getInteger("runTime", 10);
    public static final ShortTextLongConverter ShortText = ShortTextLongConverter.INSTANCE;
    public static final String PATH = System.getProperty("path", OS.TMP);
    public static final boolean ACCOUNT_FOR_COORDINATED_OMISSION = Jvm.getBoolean("accountForCoordinatedOmission");

    @SuppressWarnings("try")
    public static void main(String[] args) {
        printProperties();

        String tmpDir = PATH + "/bench-" + System.nanoTime();
        try (ChronicleQueue input = single(tmpDir, "/input");
             ChronicleQueue output = single(tmpDir, "/output")) {

            // processing thread
            Thread processor = new Thread(() -> {
                try (AffinityLock ignored = AffinityLock.acquireCore()) {
                    OMSOut out = output.createAppender().methodWriter(OMSOut.class);
                    OMSImpl oms = new OMSImpl(out);
                    MethodReader in = input.createTailer("test").methodReader(oms);
                    while (!Thread.currentThread().isInterrupted()) {
                        in.readOne();
                    }
                }
            }, "processor");
            processor.start();

            JLBH jlbh = new JLBH(new JLBHOptions()
                    .warmUpIterations(300_000)
                    .pauseAfterWarmupMS(500)
                    .throughput(THROUGHPUT)
                    .iterations(THROUGHPUT * RUN_TIME)
                    .runs(5)
                    .recordOSJitter(false)
                    .accountForCoordinatedOmission(ACCOUNT_FOR_COORDINATED_OMISSION)
                    .acquireLock(AffinityLock::acquireCore)
                    .jlbhTask(new MyJLBHTask(input)));

            Thread last = new Thread(() -> {
                try (AffinityLock ignored = AffinityLock.acquireCore()) {
                    final MethodReader reader = output.createTailer().methodReader(new OMSOut() {
                        @Override
                        public void executionReport(ExecutionReport er) {
                            jlbh.sampleNanos(System.nanoTime() - er.sendingTime());
                        }

                        @Override
                        public void orderCancelReject(OrderCancelReject ocr) {
                        }

                        @Override
                        public void jvmError(String msg) {
                        }
                    });
                    while (!Thread.currentThread().isInterrupted())
                        reader.readOne();

                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }, "last");
            last.start();

            jlbh.start();

            processor.interrupt();
            last.interrupt();
        }
        printProperties();
        Jvm.pause(1000);
        IOTools.deleteDirWithFiles(tmpDir);
        // for a clean shutdown
        BackgroundResourceReleaser.stop();
        DiskSpaceMonitor.INSTANCE.close();
    }

    static ChronicleQueue single(String tmpDir, String x) {
        return ChronicleQueue.singleBuilder(tmpDir + x)
                .blockSize(OS.isSparseFileSupported() ? 64L << 30 : 64L << 20)
                .build();
    }

    private static void printProperties() {
        long estimatedMemory = Math.round(Runtime.getRuntime().totalMemory() / 1e6);
        System.out.println("-Xmx" + estimatedMemory + "m " +
                "-DbyteInBinary=" + AbstractEvent.BYTES_IN_BINARY + " " +
                "-DpregeneratedMarshallable=" + AbstractEvent.PREGENERATED_MARSHALLABLE + " " +
                "-Dthroughput=" + THROUGHPUT + " " +
                "-DrunTime=" + RUN_TIME + " " +
                "-Dpath=" + PATH + " " +
                "-DaccountForCoordinatedOmission=" + ACCOUNT_FOR_COORDINATED_OMISSION);
    }

    private static class MyJLBHTask implements JLBHTask {
        private JLBH jlbh;
        private NewOrderSingle nos;
        private OMSIn in;

        public MyJLBHTask(ChronicleQueue input) {
            nos = new NewOrderSingle()
                    .sender(ShortText.parse("client"))
                    .target(ShortText.parse("OMS"))
                    .clOrdID("clOrdID")
                    .orderQty(1e6)
                    .price(1.6)
                    .symbol(ShortText.parse("AUDUSD"))
                    .ordType(OrderType.LIMIT)
                    .side(Side.SELL);
            in = input.createAppender().methodWriter(OMSIn.class);
        }

        @Override
        public void init(JLBH jlbh) {
            this.jlbh = jlbh;
        }

        @Override
        public void run(long startTimeNS) {
            nos.sendingTime(startTimeNS).transactTime(now());
            in.newOrderSingle(nos);
        }
    }
}
