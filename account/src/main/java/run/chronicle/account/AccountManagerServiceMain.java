package run.chronicle.account;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.threads.Pauser;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

public class AccountManagerServiceMain {
    static final String serviceId = System.getProperty("serviceId", "account-manager");
    static final String inputQueue = System.getProperty("inputQueue", "account-in");
    static final String outputQueue = System.getProperty("outputQueue", "account-out");

    public static void main(String[] args) {
        Pauser pauser = Pauser.balanced();
        try (ChronicleQueue inQ = ChronicleQueue.single(inputQueue);
             ChronicleQueue outQ = ChronicleQueue.single(outputQueue)) {
            AccountManagerOut out = outQ.methodWriter(AccountManagerOut.class);
            AccountManagerImpl accountManager = new AccountManagerImpl(out);
            MethodReader reader = inQ.createTailer(serviceId).methodReader(accountManager);
            Jvm.startup().on(AccountManagerServiceMain.class, "starting serviceId: " + serviceId);
            while (true) {
                if (reader.readOne())
                    pauser.reset();
                else
                    pauser.pause();
            }
        }
    }
}
