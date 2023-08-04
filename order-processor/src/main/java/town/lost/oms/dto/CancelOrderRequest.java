/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.converter.Base85;

/**
 * The {@code CancelOrderRequest} class represents a request to cancel an order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link CancelOrderRequest}.
 * This indicates that the event will be processed into a {@link CancelOrderRequest} that represents a request to cancel an order.</p>
 *
 * <p>Each {@code CancelOrderRequest} contains the symbol of the financial instrument for the order that is being requested to be cancelled and the client order ID.</p>
 *
 * <p>The symbol is encoded using Base85 to save space, and the client order ID is a string that identifies the order.</p>
 */
public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol of the financial instrument for the order. Encoded using Base85.
    @Base85
    private long symbol;
    // Client order ID.
    private String clOrdID = "";

    /**
     * Retrieves the client order ID.
     *
     * @return The client order ID.
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
     * Retrieves the symbol of the financial instrument for the order.
     *
     * @return The symbol of the financial instrument for the order.
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

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
            out.write("clOrdID").object(String.class, clOrdID);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
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

    @SuppressWarnings("unchecked")
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
}
