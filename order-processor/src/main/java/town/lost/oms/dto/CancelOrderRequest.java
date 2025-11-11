/*
 * Copyright 2016-2025 chronicle.software
 */
package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.ShortTextLongConverter;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.converter.ShortText;

/**
 * The {@code CancelOrderRequest} class represents a request to cancel an order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link CancelOrderRequest}.
 * This indicates that the event will be processed into a {@link CancelOrderRequest} that represents a request to cancel an order.
 *
 * <p>Each {@code CancelOrderRequest} contains the symbol of the financial instrument for the order that is being requested to be cancelled and the client order ID.
 *
 * The {@code CancelOrderRequest} class parallels a FIX 4.2 "Order Cancel Request" (MsgType=35=F).
 * <p>
 * Typical FIX tag mappings:
 * <ul>
 *   <li>{@code clOrdID} (FIX 11) - New unique client ID for this cancel request.</li>
 *   <li>{@code origClOrdID} (FIX 41) - Original client ID of the order to be canceled.</li>
 *   <li>{@code symbol} (FIX 55) - Symbol of the order to be canceled.</li>
 *   <li>{@code side} (FIX 54) - Side of the order (buy/sell).</li>
 *   <li>{@code account} (FIX 1) - Account under which the original order was placed.</li>
 *   <li>{@code sendingTime} (FIX 52) - Time the cancel request is being sent.</li>
 *   <li>{@code transactTime} (FIX 60, optional) - May store the transaction time for the request.</li>
 *   <li>{@code sender}/{@code target} - Conceptually match SenderCompID (49) / TargetCompID (56).</li>
 * </ul>
 * If the request cannot be fulfilled, an {@code OrderCancelReject} (MsgType=35=9) may result,
 * indicating why the cancel was rejected.
 *
 * <p>The {@code symbol} and {@code account} fields are encoded using {@link ShortTextLongConverter}
 * to save space, while the client order IDs are strings for identification purposes.
 */
public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol of the financial instrument for the order.
    @ShortText
    private long symbol;
    @ShortText
    private long account;

    // Client order ID.
    private String clOrdID = "";

    // Original client order ID.
    private String origClOrdID = "";

    // Side of the order (e.g., buy or sell).
    private Side side;


    /**
     * Retrieves the symbol of the financial instrument for the order.
     *
     * @return The symbol as a {@code long}.
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the financial instrument for the order and returns the {@code CancelOrderRequest} object. This method allows for method chaining.
     *
     * @param symbol The symbol of the financial instrument for the order.
     * @return The {@code CancelOrderRequest} object with the symbol set.
     */
    public CancelOrderRequest symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }


    /**
     * Retrieves the account associated with the order.
     *
     * @return The account as a {@code long}.
     */
    public long account() {
        return account;
    }

    /**
     * Sets the account associated with the order.
     *
     * @param account The account to set.
     * @return This {@code CancelOrderRequest} instance for method chaining.
     */
    public CancelOrderRequest account(long account) {
        this.account = account;
        return this;
    }

    /**
     * Retrieves the client order ID.
     *
     * @return The client order ID as a {@code String}.
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Sets the client order ID and returns the {@code CancelOrderRequest} object. This method allows for method chaining.
     *
     * @param clOrdID The client order ID.
     * @return The {@code CancelOrderRequest} object with the client order ID set.
     */
    public CancelOrderRequest clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Retrieves the original client order ID.
     *
     * @return The original client order ID as a {@code String}.
     */
    public String origClOrdID() {
        return origClOrdID;
    }

    /**
     * Sets the original client order ID.
     *
     * @param origClOrdID The original client order ID to set.
     * @return This {@code CancelOrderRequest} instance for method chaining.
     */
    public CancelOrderRequest origClOrdID(String origClOrdID) {
        this.origClOrdID = origClOrdID;
        return this;
    }

    /**
     * Retrieves the side of the order.
     *
     * @return The side of the order as a {@link Side} enum.
     */
    public Side side() {
        return side;
    }

    /**
     * Sets the side of the order.
     *
     * @param side The side to set (e.g., buy or sell).
     * @return This {@code CancelOrderRequest} instance for method chaining.
     */
    public CancelOrderRequest side(Side side) {
        this.side = side;
        return this;
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(ShortTextLongConverter.INSTANCE, symbol);
            out.write("account").writeLong(ShortTextLongConverter.INSTANCE, account);
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("origClOrdID").object(String.class, origClOrdID);
            out.write("side").object(Side.class, side);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(ShortTextLongConverter.INSTANCE);
            account = in.read("account").readLong(ShortTextLongConverter.INSTANCE);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            origClOrdID = in.read("origClOrdID").object(origClOrdID, String.class);
            side = in.read("side").object(Side.class);
        }
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
            out.writeLong(account);
            out.writeObject(String.class, clOrdID);
            out.writeObject(String.class, origClOrdID);
            out.writeObject(Side.class, side);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readMarshallable(BytesIn<?> in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MASHALLABLE_VERSION) {
                symbol = in.readLong();
                account = in.readLong();
                clOrdID = in.readObject(String.class);
                origClOrdID = in.readObject(String.class);
                side = in.readObject(Side.class);
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        }
    }

    /**
     * Validates the fields of this {@code CancelOrderRequest}.
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid.
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        if (symbol == 0) {
            throw new InvalidMarshallableException("symbol is required");
        }
        if (account == 0) {
            throw new InvalidMarshallableException("account is required");
        }
        if (clOrdID == null || clOrdID.isEmpty()) {
            throw new InvalidMarshallableException("clOrdID is required");
        }
        if (origClOrdID == null || origClOrdID.isEmpty()) {
            throw new InvalidMarshallableException("origClOrdID is required");
        }
        if (side == null) {
            throw new InvalidMarshallableException("side is required");
        }
    }
}
