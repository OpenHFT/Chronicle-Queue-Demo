package run.chronicle.account;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.queue.channel.PipeHandler;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;
import net.openhft.chronicle.wire.channel.ChronicleGatewayMain;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.dto.CreateAccount;
import run.chronicle.account.dto.OnCreateAccount;

public class AccountManagerClientMain {

    private static final String URL = System.getProperty("url", "tcp://localhost:" + ChronicleGatewayMain.PORT);

    public static void main(String[] args) {

        try (ChronicleContext context = ChronicleContext.newContext(URL)) {
            ChronicleChannel channel = context.newChannelSupplier(new PipeHandler().publish("account-in").subscribe("account-out")).get();

            Jvm.startup().on(AccountManagerClientMain.class, "Channel connected to: " + channel.channelCfg().hostname() + "[" + channel.channelCfg().port() + "]");

            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            CreateAccount createAccount = new CreateAccount()
                    .name("Account1")
                    .account(10001)
                    .balance(2000.0)
                    .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos());
            Jvm.startup().on(AccountManagerClientMain.class, "Create: " + createAccount.toString());
            accountManagerIn.createAccount(createAccount);

            StringBuilder evtType = new StringBuilder();
            OnCreateAccount response = channel.readOne(evtType, OnCreateAccount.class);

            Jvm.startup().on(AccountManagerClientMain.class, ">>> " + evtType + ": " + response.toString());
        }
    }
}
