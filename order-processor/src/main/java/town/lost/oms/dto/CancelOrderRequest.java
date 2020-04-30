/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;

public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
    private static final int MASHALLABLE_VERSION = 1;
    private String clOrdID = "";
    @LongConversion(Base85LongConverter.class)
    private long symbol;

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
/*
    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        out.write("clOrdID").object(String.class, clOrdID);
        out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        clOrdID = in.read("clOrdID").object(clOrdID, String.class);
        symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
    }

    @Override
    public void writeMarshallable(BytesOut out) {
        super.writeMarshallable(out);
        out.writeStopBit(MASHALLABLE_VERSION);
        out.writeObject(String.class, clOrdID);
        out.writeLong(symbol);
    }

    @Override
    public void readMarshallable(BytesIn in) {
        super.readMarshallable(in);
        int version = (int) in.readStopBit();
        if (version == MASHALLABLE_VERSION) {
            clOrdID = (String) in.readObject(String.class);
            symbol = in.readLong();
        } else {
            throw new IllegalStateException("Unknown version " + version);
        }
    }
 */
}
