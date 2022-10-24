package net.openhft.chronicle.queue.channel.accountex.main;

import net.openhft.chronicle.wire.channel.ChronicleGatewayMain;

import java.io.IOException;

public class AccountManagerSvcMain {

    static final int PORT = Integer.getInteger("port", 6662);

    public static void main(String[] args) throws IOException {
        System.setProperty("port", ""+PORT);
        ChronicleGatewayMain.main(args);
    }
}
