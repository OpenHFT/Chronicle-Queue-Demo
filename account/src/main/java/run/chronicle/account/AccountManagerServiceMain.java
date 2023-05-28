package run.chronicle.account;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.SimpleCloseable;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.threads.Pauser;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

public class AccountManagerServiceMain extends SimpleCloseable implements Runnable {
    private static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;
    private static final String SERVICE_URL = System.getProperty("serviceUrl", "internal://");

    public static void main(String[] args) {
        new AccountManagerServiceMain().run();
    }

    private static AccountManagerImpl createService(String serviceId, AccountManagerOut out) {
        return new AccountManagerImpl(out)
                .id(BASE85.parse(serviceId));
    }

    @Override
    public void run() {
        String serviceId = "service";
        Jvm.startup().on(getClass(), "starting serviceId: " + serviceId);
        Pauser pauser = Pauser.balanced();
        PipeHandler handler = new PipeHandler().publish("account-out").subscribe("account-in");
        try (ChronicleContext context = ChronicleContext.newContext(SERVICE_URL)) {
            ChronicleChannel channel = context.newChannelSupplier(handler).get();

            AccountManagerOut out = channel.methodWriter(AccountManagerOut.class);
            Object accountManager = createService(serviceId, out);
            MethodReader reader = channel.methodReader(accountManager);
            Jvm.startup().on(getClass(), "starting accountManager: " + accountManager);

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
