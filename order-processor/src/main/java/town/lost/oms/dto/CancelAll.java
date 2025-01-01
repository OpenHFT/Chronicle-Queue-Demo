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
 * The {@code CancelAll} class represents an event that is used to cancel all active orders for a particular symbol.
 *
 * <p>This class extends the {@link AbstractEvent} class with the type parameter {@link CancelAll}, indicating that
 * it is processed as a {@code CancelAll} event.
 *
 * <p>This class is typically used like this:
 *
 * <pre>
 * public void cancelAll(CancelAll cancelAll) {
 *     // Iterate over active orders and cancel those matching the symbol
 *     for (Order order : activeOrders) {
 *         if (order.symbol() == cancelAll.symbol()) {
 *             CancelOrderRequest request = new CancelOrderRequest()
 *                 .sender(cancelAll.target())
 *                 .target(cancelAll.sender())
 *                 .symbol(order.symbol())
 *                 .clOrdID(order.clOrdID())
 *                 .sendingTime(cancelAll.sendingTime())
 *                 .origClOrdID(order.origClOrdID())
 *                 .side(order.side());
 *             out.cancelOrderRequest(request);
 *         }
 *     }
 * }
 * }</pre>
 *
 * <p>Note that the {@code symbol} field is encoded using {@link ShortTextLongConverter} to minimize storage and transmission size.
 */
public class CancelAll extends AbstractEvent<CancelAll> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol for which all orders are to be canceled.
    @ShortText
    private long symbol;

    // Client order ID.
    private String clOrdID = "";

    /**
     * Get the symbol for which all orders are to be canceled.
     *
     * @return The symbol value as a long.
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Set the symbol for which all orders are to be canceled.
     *
     * @param symbol The symbol value to set, as a long.
     * @return This CancelAll instance, to facilitate method chaining.
     */
    public CancelAll symbol(long symbol) {
        this.symbol = symbol;
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
     * Sets the client order ID and returns the {@code CancelAll} object. This method allows for method chaining.
     *
     * @param clOrdID The client order ID.
     * @return The {@code CancelAll} object with the client order ID set.
     */
    public CancelAll clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(ShortTextLongConverter.INSTANCE, symbol);
            out.write("clOrdID").object(String.class, clOrdID);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(ShortTextLongConverter.INSTANCE);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
        }
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
            out.writeObject(String.class, clOrdID);
        }
    }

    @Override
    public void readMarshallable(BytesIn<?> in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MASHALLABLE_VERSION) {
                symbol = in.readLong();
                clOrdID = in.readObject(String.class);
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        }
    }

    /**
     * Validates the fields of this {@code CancelAll} event.
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid.
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate();
        if (symbol == 0) {
            throw new InvalidMarshallableException("symbol is required");
        }
        if (clOrdID == null || clOrdID.isEmpty()) {
            throw new InvalidMarshallableException("clOrdID is required");
        }
    }
}
