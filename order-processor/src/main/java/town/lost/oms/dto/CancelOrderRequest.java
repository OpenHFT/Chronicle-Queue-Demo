/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;

public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
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
}
