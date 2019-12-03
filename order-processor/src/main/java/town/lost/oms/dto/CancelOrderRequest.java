/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

public class CancelOrderRequest extends AbstractEvent<CancelOrderRequest> {
    private String clOrdID = "";

    private String symbol = "";

    public String clOrdID() {
        return clOrdID;
    }

    public CancelOrderRequest clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public String symbol() {
        return symbol;
    }

    public CancelOrderRequest symbol(String symbol) {
        this.symbol = symbol;
        return this;
    }
}
