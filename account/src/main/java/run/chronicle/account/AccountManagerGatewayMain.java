//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.channel.ChronicleGatewayMain;

import java.io.IOException;

/**
 * This class acts as the main entry point for the AccountManagerGateway,
 * which extends the ChronicleGatewayMain class.
 */
public class AccountManagerGatewayMain extends ChronicleGatewayMain {

    /**
     * Constructor for AccountManagerGatewayMain.
     *
     * @param url The URL for the ChronicleGateway.
     * @throws InvalidMarshallableException Thrown if the gateway URL is invalid.
     */
    public AccountManagerGatewayMain(String url) throws InvalidMarshallableException {
        super(url);
    }

    /**
     * Main method to start the gateway.
     *
     * @param args The command line arguments. Optionally, the first argument can be the gateway URL.
     * @throws IOException                  If an IO error occurs during gateway initialization.
     * @throws InvalidMarshallableException Thrown if the gateway URL is invalid.
     */
    public static void main(String... args) throws IOException, InvalidMarshallableException {
        // The main() method in ChronicleGatewayMain expects a factory method for creating the gateway
        // and a URL as arguments. If no command-line arguments are given, an empty string is used as the URL.
        main(AccountManagerGatewayMain.class, AccountManagerGatewayMain::new, args.length == 0 ? "" : args[0])
                .run();
    }
}
