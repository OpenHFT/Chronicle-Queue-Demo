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

/**
 * The main service class for the Account Manager application.
 */
public class AccountManagerServiceMain extends SimpleCloseable implements Runnable {
    private static final Base85LongConverter BASE85 = Base85LongConverter.INSTANCE;
    private static final String SERVICE_URL = System.getProperty("serviceUrl", "internal://");

    /**
     * Main method to start the service.
     */
    public static void main(String[] args) {
        new AccountManagerServiceMain().run();
    }

    /**
     * Create the account manager service.
     *
     * @param serviceId The id of the service to be created.
     * @param out       The output handler for the service.
     * @return The created AccountManagerImpl instance.
     */
    private static AccountManagerImpl createService(String serviceId, AccountManagerOut out) {
        return new AccountManagerImpl(out)
                .id(BASE85.parse(serviceId));
    }

    /**
     * Run the account manager service.
     */
    @Override
    public void run() {
        String serviceId = "service";
        // Logging startup information
        Jvm.startup().on(getClass(), "starting serviceId: " + serviceId);

        // Pauser for rate-limiting event processing
        Pauser pauser = Pauser.balanced();

        // Handler for publishing and subscribing to queues
        PipeHandler handler = new PipeHandler().publish("account-out").subscribe("account-in");

        // Context for interaction with the Chronicle system
        try (ChronicleContext context = ChronicleContext.newContext(SERVICE_URL)) {
            // Channel for sending and receiving messages
            ChronicleChannel channel = context.newChannelSupplier(handler).get();

            // Method writer for sending events
            AccountManagerOut out = channel.methodWriter(AccountManagerOut.class);

            // Create the account manager service
            Object accountManager = createService(serviceId, out);

            // Method reader for receiving events
            MethodReader reader = channel.methodReader(accountManager);

            // Logging account manager startup information
            Jvm.startup().on(getClass(), "starting accountManager: " + accountManager);

            // Main event processing loop
            while (!isClosed()) {
                try {
                    // If an event was processed, reset the pauser. If not, pause briefly to avoid busy-waiting.
                    if (reader.readOne())
                        pauser.reset();
                    else
                        pauser.pause();
                } catch (Throwable t) {
                    // On any exception, log the error and continue
                    out.jvmError(t.toString());
                }
            }
        }
    }
}
