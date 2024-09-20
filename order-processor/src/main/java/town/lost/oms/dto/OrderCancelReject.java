/*
 * Copyright (c) 2016-2024 Chronicle Software Ltd
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
 * The {@code OrderCancelReject} class represents an event that is used to indicate that a request to cancel an order was rejected.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link OrderCancelReject}.
 * This indicates that the event will be processed into a {@link OrderCancelReject} that will be sent to the order
 * system.
 *
 * <p>Each {@code OrderCancelReject} event contains:
 * <ul>
 *   <li><strong>symbol</strong>: The identifier for the financial instrument involved in the order.</li>
 *   <li><strong>clOrdID</strong>: The client order ID of the order that was requested to be canceled.</li>
 *   <li><strong>reason</strong>: A string explaining why the cancellation request was rejected.</li>
 * </ul>
 *
 * <p>Note that the symbol field is encoded using {@link ShortText} to minimize the space required for storage and transmission.
 */
public class OrderCancelReject extends AbstractEvent<OrderCancelReject> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol for which the order cancellation was rejected.
    @ShortText
    private long symbol;

    // Client order ID of the order that was requested to be canceled.
    private String clOrdID = "";

    // Reason for the rejection of the order cancellation request.
    private String reason = "";

    /**
     * Get the client order ID of the order that was requested to be canceled.
     *
     * @return The client order ID as a string.
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Set the client order ID of the order that was requested to be canceled.
     *
     * @param clOrdID The client order ID to set, as a string.
     * @return This OrderCancelReject instance, to facilitate method chaining.
     */
    public OrderCancelReject clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Get the symbol for which the order cancellation was rejected.
     *
     * @return The symbol value as a {@code long}.
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Set the symbol for which the order cancellation was rejected.
     *
     * @param symbol The symbol value to set, as a long.
     * @return This {@code OrderCancelReject} instance, to facilitate method chaining.
     */
    public OrderCancelReject symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Get the reason for the rejection of the order cancellation request.
     *
     * @return The reason as a string.
     */
    public String reason() {
        return reason;
    }

    /**
     * Set the reason for the rejection of the order cancellation request.
     *
     * @param reason The reason to set, as a string.
     * @return This {@code OrderCancelReject} instance, to facilitate method chaining.
     */
    public OrderCancelReject reason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public void writeMarshallable(WireOut out) {
        validate();
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(ShortTextLongConverter.INSTANCE, symbol);
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("reason").object(String.class, reason);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(ShortTextLongConverter.INSTANCE);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            reason = in.read("reason").object(reason, String.class);
        }
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        validate();
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
            out.writeObject(String.class, clOrdID);
            out.writeObject(String.class, reason);
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
                reason = in.readObject(String.class);
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        }
    }

    /**
     * Validates the fields of this {@code OrderCancelReject} event.
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid.
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate();
        if (symbol == 0)
            throw new InvalidMarshallableException("symbol is required");
        if (clOrdID == null || clOrdID.isEmpty())
            throw new InvalidMarshallableException("clOrdID is required");
        if (reason == null || reason.isEmpty())
            throw new InvalidMarshallableException("reason is required");
    }
}
