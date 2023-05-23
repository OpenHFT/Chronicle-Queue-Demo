package run.chronicle.account;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.channel.ChronicleGatewayMain;

import java.io.IOException;

public class AccountManagerGatewayMain extends ChronicleGatewayMain {
    public AccountManagerGatewayMain(String url) throws InvalidMarshallableException {
        super(url);
    }

    public static void main(String... args) throws IOException, InvalidMarshallableException {
        main(AccountManagerGatewayMain.class, AccountManagerGatewayMain::new, args.length == 0 ? "" : args[0]);
    }
}
