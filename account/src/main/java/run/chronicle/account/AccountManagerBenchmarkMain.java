package run.chronicle.account;

import net.openhft.affinity.AffinityThreadFactory;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.Transfer;
import run.chronicle.account.util.LogsAccountManagerOut;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

/*
-Xmx64m -Dthroughput=100000 -DrunTime=30 -Dbuffered=false -Durl=tcp://localhost:1248 -DaccountForCoordinatedOmission=false
-------------------------------- SUMMARY (end to end) us -------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50.0:           10.99        10.99        11.02        11.02        10.99         0.19
90.0:           18.02        17.89        15.70        11.22        11.12        28.86
99.0:           20.96        19.94        15.86        15.86        15.79        14.89
99.7:           34.11        21.02        16.11        15.92        15.89        17.73
99.9:           42.18        22.62        16.67        16.34        16.18        21.00
99.97:        2021.38        26.08        18.21        17.44        17.25        25.45
99.99:        5480.45        60.48        44.48        22.82        47.68        52.39
99.997:       6938.62       332.29       426.50        80.26       451.07        75.49
worst:        7593.98       728.06       820.22       303.62       838.66        54.02

Windows 11 laptop, i7-1360P, Java 11
-Dthroughput=10000
-------------------------------- SUMMARY (end to end) us -------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50.0:           24.42        24.29        24.42        24.42        24.29         0.35
90.0:           36.80        32.42        32.96        32.96        34.11         3.37
99.0:          221.95       152.83       156.42       163.07       232.70        25.84

-Dthroughput=20000
-------------------------------- SUMMARY (end to end) us -------------------------------------------
Percentile   run1         run2         run3         run4         run5      % Variation
50.0:           24.67        24.67        24.67        25.12        25.12         1.20
90.0:           35.78        33.34        34.62        34.37        34.24         2.50
99.0:         4124.67       373.25      2086.91       474.62       537.60        75.37
 */
public class AccountManagerBenchmarkMain {
    public static final int THROUGHPUT = Integer.getInteger("throughput", OS.isLinux() ? 100_000 : 10_000);
    public static final int RUN_TIME = Integer.getInteger("runTime", 30);
    public static final boolean BUFFERED = Jvm.getBoolean("buffered");
    public static final String URL = System.getProperty("url", "tcp://localhost:1248");
    public static final boolean ACCOUNT_FOR_COORDINATED_OMISSION = Jvm.getBoolean("accountForCoordinatedOmission");

    static {
        if (!OS.isLinux())
            System.setProperty("affinity.reserved", "0");
        System.setProperty("disable.single.threaded.check", "true");
        System.setProperty("pauser.minProcessors", "4");
    }

    @SuppressWarnings("try")
    public static void main(String[] args) throws InterruptedException {
        deleteQueues();
        printProperties();

        ExecutorService es = Executors.newCachedThreadPool(new AffinityThreadFactory("test"));

        AccountManagerServiceMain service = new AccountManagerServiceMain();
        es.submit(service);

        AccountManagerGatewayMain gateway = new AccountManagerGatewayMain(URL);
        gateway.buffered(BUFFERED);
        es.submit(gateway);

        try (ChronicleContext context = ChronicleContext.newContext(URL)) {
            ChronicleChannel channel = context.newChannelSupplier(new PipeHandler().publish("account-in").subscribe("account-out")).get();

            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostname() + "[" + channel.channelCfg().port() + "]");

            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            AccountManagerClientMain.createAccount(accountManagerIn, CLOCK.currentTimeNanos(), 1);
            AccountManagerClientMain.createAccount(accountManagerIn, CLOCK.currentTimeNanos(), 2);

            JLBH jlbh = new JLBH(new JLBHOptions()
                    .throughput(THROUGHPUT)
                    .iterations(THROUGHPUT * RUN_TIME)
                    .runs(5)
                    .recordOSJitter(false)
                    .accountForCoordinatedOmission(ACCOUNT_FOR_COORDINATED_OMISSION)
                    .jlbhTask(new MyJLBHTask(accountManagerIn)));

            es.submit(() -> {
                MethodReader reader = channel.methodReader(new LogsAccountManagerOut() {
                    @Override
                    public void onTransfer(OnTransfer onTransfer) {
                        // startTimeNS is in nanoTime()
                        long durationNs = System.nanoTime() - onTransfer.transfer().sendingTime();
                        jlbh.sample(durationNs);
                    }
                });
                while (!Thread.interrupted()) {
                    reader.readOne();
                }
            });

            jlbh.start();

        }
        gateway.close();
        service.close();
        Jvm.pause(100);
        es.shutdownNow();
        es.awaitTermination(1, TimeUnit.SECONDS);

        deleteQueues();
    }

    private static void deleteQueues() {
        IOTools.deleteDirWithFiles("account-in");
        IOTools.deleteDirWithFiles("account-out");
    }

    private static void printProperties() {
        long estimatedMemory = Math.round(Runtime.getRuntime().totalMemory() / 1e6);
        System.out.println("-Xmx" + estimatedMemory + "m " +
                "-Dthroughput=" + THROUGHPUT + " " +
                "-DrunTime=" + RUN_TIME + " " +
                "-Dbuffered=" + BUFFERED + " " +
                "-Durl=" + URL + " " +
                "-DaccountForCoordinatedOmission=" + ACCOUNT_FOR_COORDINATED_OMISSION);
    }

    private static class MyJLBHTask implements JLBHTask {
        private final AccountManagerIn input;
        private JLBH jlbh;
        private Transfer transfer = new Transfer();

        public MyJLBHTask(AccountManagerIn input) {
            this.input = input;
        }

        @Override
        public void init(JLBH jlbh) {
            this.jlbh = jlbh;
        }

        @Override
        public void run(long startTimeNS) {
            AccountManagerClientMain.transfer(input, startTimeNS, transfer, false);
        }
    }
}
