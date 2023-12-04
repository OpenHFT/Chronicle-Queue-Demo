package run.chronicle.account;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConverter;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import net.openhft.chronicle.wire.channel.ChronicleGatewayMain;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.dto.*;
import run.chronicle.account.util.LogsAccountManagerOut;

import java.util.concurrent.atomic.AtomicBoolean;
/**
 * This class acts as the main entry point for the AccountManagerClient.
 * It creates a client which connects to a Chronicle server and performs various actions.
 */
public class AccountManagerClientMain {
    private static final String URL = System.getProperty("url", "tcp://localhost:" + ChronicleGatewayMain.PORT);

    private static final LongConverter BASE85 = Base85LongConverter.INSTANCE;
    private static final long TARGET = BASE85.parse("service");
    private static final int EUR = (int) BASE85.parse("EUR");
    private static final String CLIENT = "client";
    private static final long SENDER = BASE85.parse(CLIENT);
    private static final Bytes<byte[]> REFERENCE = Bytes.from("benchmark");

    /**
     * Main method to start the client.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        // Create a new ChronicleContext using a URL and client name...
        // Obtain a ChronicleChannel...
        try (ChronicleContext context = ChronicleContext.newContext(URL).name(CLIENT)) {
            ChronicleChannel channel = context.newChannelSupplier(new PipeHandler().publish("account-in").subscribe("account-out")).get();

            // Log the hostname and port of the connected channel...
            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostPorts());

            // Create an instance of AccountManagerIn, which allows us to make calls to the server...
            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            // Generate a sending timestamp...
            long sendingTime = SystemTimeProvider.CLOCK.currentTimeNanos();
            createAccount(accountManagerIn, sendingTime, 1);
            createAccount(accountManagerIn, sendingTime, 2);
            transfer(accountManagerIn, sendingTime, new Transfer(), true);

            AtomicBoolean running = new AtomicBoolean(true);
            MethodReader reader = channel.methodReader(new LogsAccountManagerOut() {
                @Override
                public void onCreateAccount(OnCreateAccount onCreateAccount) {
                    if (onCreateAccount.target() == SENDER) {
                        Jvm.startup().on(AccountManagerClientMain.class, "onCreateAccount(" + onCreateAccount + ")");
                    }
                }

                @Override
                public void createAccountFailed(CreateAccountFailed createAccountFailed) {
                    if (createAccountFailed.target() == SENDER) {
                        Jvm.warn().on(AccountManagerClientMain.class, "createAccountFailed(" + createAccountFailed + ")");
                    }
                }

                @Override
                public void onTransfer(OnTransfer onTransfer) {
                    if (onTransfer.target() == SENDER) {
                        Jvm.startup().on(AccountManagerClientMain.class, "onTransfer(" + onTransfer + ")");
                        if (onTransfer.transfer().sendingTime() == sendingTime)
                            running.set(false);
                    }
                }

                @Override
                public void transferFailed(TransferFailed transferFailed) {
                    if (transferFailed.target() == SENDER) {
                        Jvm.warn().on(AccountManagerClientMain.class, "transferFailed(" + transferFailed + ")");
                        if (transferFailed.transfer().sendingTime() == sendingTime)
                            running.set(false);
                    }
                }
            });
            // Loop until all transfers are complete...
            while (running.get()) {
                reader.readOne();
            }
        }
    }

    /**
     * Creates an account.
     *
     * @param accountManagerIn An instance of AccountManagerIn.
     * @param sendingTime      The sending timestamp.
     * @param num              The account number.
     */
    static void createAccount(AccountManagerIn accountManagerIn, long sendingTime, int num) {
        CreateAccount createAccount = new CreateAccount()
                .sender(SENDER)
                .target(TARGET)
                .name("Account" + num)
                .account(10 + num)
                .balance(1e9)
                .currency(EUR)
                .sendingTime(sendingTime);
        Jvm.startup().on(AccountManagerClientMain.class, "Create: " + createAccount);
        accountManagerIn.createAccount(createAccount);
    }

    /**
     * Performs a transfer.
     *
     * @param accountManagerIn An instance of AccountManagerIn.
     * @param sendingTime      The sending timestamp.
     * @param transfer         An instance of Transfer.
     * @param log              A boolean indicating whether to log the transfer.
     */
    static void transfer(AccountManagerIn accountManagerIn, long sendingTime, Transfer transfer, boolean log) {
        transfer
                .sender(SENDER)
                .target(TARGET)
                .from(11)
                .to(12)
                .amount(0.01)
                .currency(EUR)
                .sendingTime(sendingTime)
                .reference(REFERENCE);
        if (log)
            Jvm.startup().on(AccountManagerClientMain.class, "Transfer: " + transfer);
        accountManagerIn.transfer(transfer);
    }
}
