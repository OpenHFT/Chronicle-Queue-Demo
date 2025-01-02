package run.chronicle.account;

import net.openhft.affinity.AffinityThreadFactory;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.OS;
import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.core.io.ClosedIORuntimeException;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.jlbh.JLBH;
import net.openhft.chronicle.jlbh.JLBHOptions;
import net.openhft.chronicle.jlbh.JLBHTask;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import net.openhft.chronicle.wire.channel.impl.internal.Handler;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.Transfer;
import run.chronicle.account.util.LogsAccountManagerOut;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

@SuppressWarnings("deprecation")
public class AccountManagerBenchmarkMain {
    public static final int THROUGHPUT = Integer.getInteger("throughput", OS.isLinux() ? 100_000 : 10_000);
    public static final int RUN_TIME = Integer.getInteger("runTime", 30);
    public static final boolean BUFFERED = Jvm.getBoolean("buffered");
    public static final String URL = System.getProperty("url", "tcp://:1248");
    public static final boolean ACCOUNT_FOR_COORDINATED_OMISSION = Jvm.getBoolean("accountForCoordinatedOmission");

    static {
        if (!OS.isLinux())
            System.setProperty("affinity.reserved", "0");
        System.setProperty("disable.single.threaded.check", "true");
        System.setProperty("pauser.minProcessors", "4");
        Handler.init();
    }

    @SuppressWarnings("try")
    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        // Delete existing queues if any before starting. This is typically done to clean up any leftover data from previous runs.
        deleteQueues();

        // Print out system properties used to configure this benchmark
        printProperties();

        // Create a new ExecutorService with a custom thread factory named 'test'.
        // This ExecutorService is used for running the client/gateway/service in the current process
        ExecutorService es = Executors.newCachedThreadPool(new AffinityThreadFactory("test"));

        // Initialise a new instance of AccountManagerServiceMain. This is the main service for managing accounts.
        AccountManagerServiceMain service = null;

        // Check if the host part of the URL is empty. If it is, that means we are running the service locally.
        if (net.openhft.chronicle.wire.channel.ChronicleContext.urlFor(URL).getHost().isEmpty()) {
            service = new AccountManagerServiceMain();
            // Submit the service to run in the ExecutorService.
            // The 'wrap' method is used to ensure any Throwable are logged instead of added to the discarded Future silently
            es.submit(wrap(service));
        } else {
            // If the URL is not empty, inform the user to start the Gateway and Service in another process.
            Jvm.startup().on(AccountManagerBenchmarkMain.class,
                    "You need to start the Gateway and Service in another process first.");
        }

        // Use a ChronicleContext to connect to the service.
        // ChronicleContext is a part of the Chronicle network library
        // which provides high performance, low latency networking capabilities.
        try (ChronicleContext context = net.openhft.chronicle.wire.channel.ChronicleContext.newContext(URL)) {
            net.openhft.chronicle.wire.channel.ChronicleChannel channel = context.newChannelSupplier(
                    new PipeHandler().publish("account-in").subscribe("account-out")).get();

            // Log the connection details.
            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostPorts());

            // Create an AccountManagerIn instance to interact with the service.
            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            // Create some accounts.
            AccountManagerClientMain.createAccount(accountManagerIn, CLOCK.currentTimeNanos(), 1);
            AccountManagerClientMain.createAccount(accountManagerIn, CLOCK.currentTimeNanos(), 2);

            // Prepare JLBH for benchmarking.
            JLBH jlbh = new JLBH(new JLBHOptions()
                    .throughput(THROUGHPUT)
                    .iterations(THROUGHPUT * RUN_TIME)
                    .runs(5)
                    .recordOSJitter(false)
                    .accountForCoordinatedOmission(ACCOUNT_FOR_COORDINATED_OMISSION)
                    .jlbhTask(new MyJLBHTask(accountManagerIn)));

            // Submit a new task to the executor service. This task reads from the channel and records benchmarks.
            es.submit(wrap(() -> {
                MethodReader reader = channel.methodReader(new LogsAccountManagerOut() {
                    @Override
                    public void onTransfer(OnTransfer onTransfer) {
                        // startTimeNS is in nanoTime()
                        long durationNs = System.nanoTime() - onTransfer.transfer().sendingTime();
                        jlbh.sample(durationNs);
                    }
                });

                // Keep reading until interrupted.
                while (!Thread.interrupted()) {
                    reader.readOne();
                }
            }));

            // Start the benchmark.
            jlbh.start();

            // Cleanup: Close the service and shutdown the ExecutorService.
            Closeable.closeQuietly(service);
            Jvm.pause(100);
            es.shutdownNow();
            es.awaitTermination(1, TimeUnit.SECONDS);
        }

        // Delete the queues after finishing. This is done to clean up any data that was created during the run.
        deleteQueues();
    }

    static Runnable wrap(Runnable runnable) {
        return () -> {
            try {
                runnable.run();
            } catch (ClosedIORuntimeException ignored) {
            } catch (Throwable t) {
                Jvm.warn().on(runnable.getClass(), t);
            }
        };
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
