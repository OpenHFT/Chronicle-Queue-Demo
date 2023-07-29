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
 * The {@code CancelAll} class represents an event that is used to cancel all active orders for a particular symbol.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link CancelOrderRequest}.
 * This indicates that the event will be processed into a {@link CancelOrderRequest} that will be sent to the order
 * system.</p>
 *
 * <p>This class is typically used like this:</p>
 *
 * <pre>
 * public void cancelAll(CancelAll cancelAll) {
 *    ocr.sender(cancelAll.target())
 *        .target(cancelAll.sender())
 *        .symbol(cancelAll.symbol())
 *        .clOrdID("")
 *        .sendingTime(cancelAll.sendingTime())
 *        .reason("No such orders");
 *    out.orderCancelReject(ocr);
 * }
 * </pre>
 *
 * <p>Note that the symbol field is encoded using Base85 to minimize the space required for storage and transmission.</p>
 */
public class CancelAll extends AbstractEvent<CancelOrderRequest> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol for which all orders are to be canceled. Encoded using Base85.
    @Base85
    private long symbol;

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

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
        }
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
        }
    }

    @Override
    public void readMarshallable(BytesIn<?> in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MASHALLABLE_VERSION) {
                symbol = in.readLong();
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        }
    }
}
