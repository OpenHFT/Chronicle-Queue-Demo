package run.chronicle.account;

import net.openhft.affinity.AffinityThreadFactory;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.threads.PauserMode;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.Transfer;
import run.chronicle.account.impl.LogsAccountManagerOut;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

public class AccountManagerBenchmarkMain {
    public static final int THROUGHPUT = Integer.getInteger("throughput", 100_000);
    public static final int RUN_TIME = Integer.getInteger("runTime", 10);
    public static final boolean BUFFERED = Jvm.getBoolean("buffered");
    public static final String URL = System.getProperty("url", "tcp://localhost:1248");
    public static final boolean ACCOUNT_FOR_COORDINATED_OMISSION = Jvm.getBoolean("accountForCoordinatedOmission");

    static {
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
        gateway.pauserMode(PauserMode.busy).buffered(BUFFERED);
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
