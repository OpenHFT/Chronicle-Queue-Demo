/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;

public class OrderCancelReject extends AbstractEvent<OrderCancelReject> {
    private static final int MASHALLABLE_VERSION = 1;
    private String clOrdID = "";
    @LongConversion(Base85LongConverter.class)
    private long symbol;
    private String reason = "";

    public String clOrdID() {
        return clOrdID;
    }

    public OrderCancelReject clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public long symbol() {
        return symbol;
    }

    public OrderCancelReject symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    public String reason() {
        return reason;
    }

    public OrderCancelReject reason(String reason) {
        this.reason = reason;
        return this;
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
            out.write("reason").object(String.class, reason);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
            reason = in.read("reason").object(reason, String.class);
        }
    }

    @Override
    public void writeMarshallable(BytesOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeObject(String.class, clOrdID);
            out.writeLong(symbol);
            out.writeObject(String.class, reason);
        }
    }

    @Override
    public void readMarshallable(BytesIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MASHALLABLE_VERSION) {
                clOrdID = (String) in.readObject(String.class);
                symbol = in.readLong();
                reason = (String) in.readObject(String.class);
            } else {
                throw new IllegalStateException("Unknown version " + version);
            }
        }
    }
}
