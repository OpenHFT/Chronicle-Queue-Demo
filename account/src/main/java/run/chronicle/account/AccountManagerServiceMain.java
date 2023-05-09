package run.chronicle.account;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.chronicle.wire.Base85LongConverter;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

public class AccountManagerServiceMain {
    public static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;
    static final String inputQueue = System.getProperty("inputQueue", "account-in");
    static final String outputQueue = System.getProperty("outputQueue", "account-out");
    static final String serviceId = System.getProperty("serviceId", "manager");

    public static void main(String[] args) {
        Pauser pauser = Pauser.balanced();
        try (ChronicleQueue inQ = ChronicleQueue.single(inputQueue);
             ChronicleQueue outQ = ChronicleQueue.single(outputQueue)) {
            AccountManagerOut out = outQ.methodWriter(AccountManagerOut.class);
            AccountManagerImpl accountManager = new AccountManagerImpl(out).id(BASE85.parse(serviceId));
            MethodReader reader = inQ.createTailer(serviceId).methodReader(accountManager);
            Jvm.startup().on(AccountManagerServiceMain.class, "starting serviceId: " + serviceId);
            while (true) {
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
