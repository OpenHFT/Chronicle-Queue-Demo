package run.chronicle.account;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.SimpleCloseable;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.chronicle.wire.Base85LongConverter;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

public class AccountManagerServiceMain extends SimpleCloseable implements Runnable {
    public static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;
    static final String inputQueue = System.getProperty("inputQueue", "account-in");
    static final String outputQueue = System.getProperty("outputQueue", "account-out");
    static final String serviceId = System.getProperty("serviceId", "manager");

    public static void main(String[] args) {
        new AccountManagerServiceMain().run();
    }

    @Override
    public void run() {
        Jvm.startup().on(getClass(), "Starting  " + this);
        Pauser pauser = Pauser.balanced();
        try (ChronicleQueue inQ = ChronicleQueue.single(inputQueue);
             ChronicleQueue outQ = ChronicleQueue.single(outputQueue)) {

            ExcerptAppender appender = outQ.acquireAppender();
            appender.singleThreadedCheckDisabled(true); // assume we are thread safe
            AccountManagerOut out = appender.methodWriter(AccountManagerOut.class);

            AccountManagerImpl accountManager =
                    new AccountManagerImpl(out)
                            .id(BASE85.parse(serviceId));

            ExcerptTailer tailer = inQ.createTailer(serviceId);
            tailer.singleThreadedCheckDisabled(true); // assume we are thread safe
            MethodReader reader = tailer.methodReader(accountManager);

            Jvm.startup().on(
                    AccountManagerServiceMain.class,
                    "starting serviceId: " + serviceId);

            while (!isClosed()) {
                try {
                    if (reader.readOne())
                        pauser.reset();
                    else
                        pauser.pause();
                } catch (Throwable t) {
                    out.jvmError(t.toString());
                }
            }
        }
    }
}
