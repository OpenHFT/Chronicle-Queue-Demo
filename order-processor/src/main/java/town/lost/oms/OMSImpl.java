/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.time.SystemTimeProvider;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.*;

/**
 * OMSImpl is a concrete implementation of the {@link OMSIn} interface, acting as an intermediary between client requests and the {@link OMSOut}.
 * <p>
 * The class processes requests for order creation, cancellation and complete cancellation.
 * <p>
 * {@link ExecutionReport} and {@link OrderCancelReject} are instantiated as instance variables to prevent unnecessary instantiation on every method call.
 */
public class OMSImpl implements OMSIn {
    // The outbound interface for sending execution reports and order cancel rejections
    private final OMSOut out;

    // Reusable instance of ExecutionReport for creating new orders
    private final ExecutionReport er = new ExecutionReport();

    // Reusable instance of OrderCancelReject for cancelling orders
    private final OrderCancelReject ocr = new OrderCancelReject();

    /**
     * Constructs a new OMSImpl with a given outbound interface.
     *
     * @param out the outbound interface to be used for sending responses
     */
    public OMSImpl(OMSOut out) {
        this.out = out;
    }

    /**
     * Processes a new order request. The request's details are populated into an execution report and sent out.
     *
     * @param nos the new order request to process
     */
    @Override
    public void newOrderSingle(NewOrderSingle nos) {
        er.reset();
        final long orderID = SystemTimeProvider.CLOCK.currentTimeNanos(); // Generate unique order ID

        // Populate the ExecutionReport with request details
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

        // Send execution report
        out.executionReport(er);
    }

    /**
     * Processes a cancellation request for an order. The request's details are populated into an order cancellation rejection and sent out.
     *
     * @param cor the cancellation request to process
     */
    @Override
    public void cancelOrderRequest(CancelOrderRequest cor) {
        // Populate OrderCancelReject with request details
        ocr.sender(cor.target())
                .target(cor.sender())
                .symbol(cor.symbol())
                .clOrdID(cor.clOrdID())
                .sendingTime(cor.sendingTime())
                .reason("No such order");

        // Send order cancellation rejection
        out.orderCancelReject(ocr);
    }

    /**
     * Processes a cancellation request for all orders. The request's details are populated into an order cancellation rejection and sent out.
     *
     * @param cancelAll the cancellation request to process
     */
    @Override
    public void cancelAll(CancelAll cancelAll) {
        // Populate OrderCancelReject with request details
        ocr.sender(cancelAll.target())
                .target(cancelAll.sender())
                .symbol(cancelAll.symbol())
                .clOrdID("")
                .sendingTime(cancelAll.sendingTime())
                .reason("No such orders");

        // Send order cancellation rejection
        out.orderCancelReject(ocr);
    }
}

