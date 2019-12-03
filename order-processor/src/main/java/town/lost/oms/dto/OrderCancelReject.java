/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

public class OrderCancelReject extends AbstractEvent<OrderCancelReject> {
    private String clOrdID = "";

    private String symbol = "";

    private String reason = "";

    public String clOrdID() {
        return clOrdID;
    }

    public OrderCancelReject clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public String symbol() {
        return symbol;
    }

    public OrderCancelReject symbol(String symbol) {
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
