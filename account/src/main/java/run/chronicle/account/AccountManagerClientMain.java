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
import run.chronicle.account.impl.LogsAccountManagerOut;

import java.util.concurrent.atomic.AtomicBoolean;

public class AccountManagerClientMain {
    private static final String senderId = System.getProperty("senderId", "test");

    private static final String URL = System.getProperty("url", "tcp://localhost:" + ChronicleGatewayMain.PORT);

    private static final LongConverter BASE85 = Base85LongConverter.INSTANCE;
    public static final long MANAGER = BASE85.parse("manager");
    public static final int EUR = (int) BASE85.parse("EUR");
    public static final long SENDER = BASE85.parse(senderId);
    public static final Bytes<byte[]> REFERENCE = Bytes.from("test");

    public static void main(String[] args) {

        try (ChronicleContext context = ChronicleContext.newContext(URL)) {
            ChronicleChannel channel = context.newChannelSupplier(new PipeHandler().publish("account-in").subscribe("account-out")).get();

            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostname() + "[" + channel.channelCfg().port() + "]");

            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

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
            while (running.get()) {
                reader.readOne();
            }
        }
    }

    static void createAccount(AccountManagerIn accountManagerIn, long sendingTime, int num) {
        CreateAccount createAccount = new CreateAccount()
                .sender(SENDER)
                .target(MANAGER)
                .name("Account" + num)
                .account(10 + num)
                .balance(1e9)
                .currency(EUR)
                .sendingTime(sendingTime);
        Jvm.startup().on(AccountManagerClientMain.class, "Create: " + createAccount);
        accountManagerIn.createAccount(createAccount);
    }


    static void transfer(AccountManagerIn accountManagerIn, long sendingTime, Transfer transfer, boolean log) {
        transfer
                .sender(SENDER)
                .target(MANAGER)
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
