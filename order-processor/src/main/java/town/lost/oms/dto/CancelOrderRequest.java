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

public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
    private static final int MASHALLABLE_VERSION = 1;
    @Base85
    private long symbol;
    private String clOrdID = "";

    public String clOrdID() {
        return clOrdID;
    }

    public CancelOrderRequest clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public long symbol() {
        return symbol;
    }

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
