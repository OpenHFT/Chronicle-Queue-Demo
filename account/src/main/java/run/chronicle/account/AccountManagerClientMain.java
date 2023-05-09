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
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.dto.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class AccountManagerClientMain {
    private static final String senderId = System.getProperty("senderId", "test");

    private static final String URL = System.getProperty("url", "tcp://localhost:" + ChronicleGatewayMain.PORT);

    private static final LongConverter BASE85 = Base85LongConverter.INSTANCE;
    public static final long SENDER = BASE85.parse(senderId);

    public static void main(String[] args) {

        try (ChronicleContext context = ChronicleContext.newContext(URL)) {
            ChronicleChannel channel = context.newChannelSupplier(new PipeHandler().publish("account-in").subscribe("account-out")).get();

            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostname() + "[" + channel.channelCfg().port() + "]");

            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            long sendingTime = SystemTimeProvider.CLOCK.currentTimeNanos();
            createAccount(accountManagerIn, sendingTime, 1);
            createAccount(accountManagerIn, sendingTime, 2);
            transfer(accountManagerIn, sendingTime);

            AtomicBoolean running = new AtomicBoolean(true);
            MethodReader reader = channel.methodReader(new AccountManagerOut() {
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
                public void startCheckpoint(CheckPoint checkPoint) {
                }

                @Override
                public void endCheckpoint(CheckPoint checkPoint) {

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

                @Override
                public void jvmError(String msg) {
                    Jvm.warn().on(AccountManagerClientMain.class, "jvmError(" + msg + ")");
                }
            });
            while (running.get()) {
                reader.readOne();
            }
        }
    }

    private static void createAccount(AccountManagerIn accountManagerIn, long sendingTime, int num) {
        CreateAccount createAccount = new CreateAccount()
                .sender(SENDER)
                .target(BASE85.parse("manager"))
                .name("Account" + num)
                .account(10 + num)
                .balance(2000.0)
                .currency((int) BASE85.parse("EUR"))
                .sendingTime(sendingTime);
        Jvm.startup().on(AccountManagerClientMain.class, "Create: " + createAccount);
        accountManagerIn.createAccount(createAccount);
    }


    private static void transfer(AccountManagerIn accountManagerIn, long sendingTime) {
        Transfer transfer = new Transfer()
                .sender(SENDER)
                .target(BASE85.parse("manager"))
                .from(11)
                .to(12)
                .amount(100.0)
                .currency((int) BASE85.parse("EUR"))
                .sendingTime(sendingTime)
                .reference(Bytes.from("test"));
        Jvm.startup().on(AccountManagerClientMain.class, "Transfer: " + transfer);
        accountManagerIn.transfer(transfer);
    }
}
