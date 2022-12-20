/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.time.SystemTimeProvider;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.*;

public class OMSImpl implements OMSIn {
    private final OMSOut out;
    private final ExecutionReport er = new ExecutionReport();
    private final OrderCancelReject ocr = new OrderCancelReject();

    public OMSImpl(OMSOut out) {
        this.out = out;
    }

    @Override
    public void newOrderSingle(NewOrderSingle nos) {
        er.reset();
        final long orderID = SystemTimeProvider.CLOCK.currentTimeNanos();
        er.sender(nos.target())
                .target(nos.sender())
                .symbol(nos.symbol())
                .clOrdID(nos.clOrdID())
                .ordType(nos.ordType())
                .orderQty(nos.orderQty())
                .price(nos.price())
                .side(nos.side())
                .sendingTime(nos.sendingTime())
                .transactTime(nos.transactTime())
                .leavesQty(0)
                .orderID(orderID)
                .text("Not ready");
        out.executionReport(er);
    }

    @Override
    public void cancelOrderRequest(CancelOrderRequest cor) {
        ocr.sender(cor.target())
                .target(cor.sender())
                .symbol(cor.symbol())
                .clOrdID(cor.clOrdID())
                .sendingTime(cor.sendingTime())
                .reason("No such order");
        out.orderCancelReject(ocr);
    }

    @Override
    public void cancelAll(CancelAll cancelAll) {
        ocr.sender(cancelAll.target())
                .target(cancelAll.sender())
                .symbol(cancelAll.symbol())
                .clOrdID("")
                .sendingTime(cancelAll.sendingTime())
                .reason("No such orders");
        out.orderCancelReject(ocr);
    }
}
