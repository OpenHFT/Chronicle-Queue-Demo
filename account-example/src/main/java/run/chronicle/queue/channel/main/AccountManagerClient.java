package run.chronicle.queue.channel.main;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import run.chronicle.queue.channel.AccountSvcHandler;
import run.chronicle.queue.channel.api.AccountManagerIn;
import run.chronicle.queue.channel.api.CreateAccount;
import run.chronicle.queue.channel.api.OnCreateAccount;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConverter;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleContext;

public class AccountManagerClient {

    private static final String URL = System.getProperty("url", "tcp://localhost:" + AccountManagerSvcMain.PORT);

    static LongConverter base85LongConverter = Base85LongConverter.INSTANCE;

    public static void main(String[] args) {

        try (ChronicleContext context = ChronicleContext.newContext(URL)) {
            ChronicleChannel channel = context.newChannelSupplier(new AccountSvcHandler()).get();

            Jvm.startup().on(AccountManagerClient.class, "Channel connected to: " + channel.channelCfg().hostname() + "[" + channel.channelCfg().port() + "]");

            final AccountManagerIn accountManagerIn = channel.methodWriter(AccountManagerIn.class);

            CreateAccount createAccount = new CreateAccount()
                                                .name(base85LongConverter.parse("Account1"))
                                                .balance(2000.0)
                                                .time(SystemTimeProvider.CLOCK.currentTimeNanos());
            Jvm.startup().on(AccountManagerClient.class, "Create: " + createAccount.toString());
            accountManagerIn.createAccount(createAccount);

            StringBuilder evtType = new StringBuilder();
            OnCreateAccount response = channel.readOne(evtType, OnCreateAccount.class);

            Jvm.startup().on(AccountManagerIn.class, ">>> " + evtType + ": " + response.toString());
        }
    }
}
