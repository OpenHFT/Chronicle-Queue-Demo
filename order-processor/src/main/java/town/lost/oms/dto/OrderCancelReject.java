//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

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
 * The {@code OrderCancelReject} class represents an event that is used to indicate that a request to cancel an order was rejected.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link OrderCancelReject}.
 * This indicates that the event will be processed into a {@link OrderCancelReject} that will be sent to the order
 * system.
 *
 * The {@code OrderCancelReject} class corresponds to a FIX 4.2 "Order Cancel Reject" (MsgType=35=9).
 * <p>
 * Typical FIX tag mappings:
 * <ul>
 *   <li>{@code symbol} (FIX 55) - Symbol for the order whose cancellation is rejected.</li>
 *   <li>{@code clOrdID} (FIX 11) - The client order ID referencing which cancel request is rejected.</li>
 *   <li>{@code reason} could map to {@code CxlRejReason (102)} or {@code Text (58)} for free-form explanation.</li>
 *   <li>{@code sender}/{@code target} - Conceptually align with SenderCompID (49) / TargetCompID (56).</li>
 *   <li>{@code sendingTime} (FIX 52, optional) - Timestamp for when the reject was emitted.</li>
 * </ul>
 * Generally used if a {@code CancelOrderRequest} (35=F) is invalid or cannot be satisfied.
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
