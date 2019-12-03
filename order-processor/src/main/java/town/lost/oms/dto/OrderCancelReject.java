/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;

public class OrderCancelReject extends AbstractEvent<OrderCancelReject> {
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
}
