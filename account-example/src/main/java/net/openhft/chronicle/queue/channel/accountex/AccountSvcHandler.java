package net.openhft.chronicle.queue.channel.accountex;

import net.openhft.chronicle.queue.channel.accountex.api.AccountManagerOut;
import net.openhft.chronicle.wire.channel.AbstractHandler;
import net.openhft.chronicle.wire.channel.ChronicleChannel;
import net.openhft.chronicle.wire.channel.ChronicleChannelCfg;
import net.openhft.chronicle.wire.channel.ChronicleContext;

public class AccountSvcHandler extends AbstractHandler<AccountSvcHandler> {

    public void run(ChronicleContext context, ChronicleChannel channel) {
        channel.eventHandlerAsRunnable(
            new AccountManager(channel.methodWriter(AccountManagerOut.class))
        ).run();
    }

    public ChronicleChannel asInternalChannel(ChronicleContext context, ChronicleChannelCfg channelCfg ) {
        throw new UnsupportedOperationException("Internal Channel not supported");
    }
}
