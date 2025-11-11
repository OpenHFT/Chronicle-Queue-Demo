/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */

package town.lost.oms;

import net.openhft.chronicle.core.time.SystemTimeProvider;
import town.lost.oms.api.OMSIn;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.*;

/**
 * The {@code OMSImpl} class is a concrete implementation of the {@link OMSIn} interface,
 * acting as an intermediary between client requests and the {@link OMSOut} interface.
 *
 * <p>This class processes requests for order creation, order cancellation, and cancelling all orders.
 * It generates appropriate responses using {@link ExecutionReport} and {@link OrderCancelReject}
 * and sends them through the {@link OMSOut} interface.
 *
 * <p><strong>Note:</strong> This class is not thread-safe. If multiple threads are expected to use
 * the same instance of {@code OMSImpl}, synchronization or separate instances per thread should be used.
 *
 * <h2>Usage Example:</h2>
 *
 * <pre>{@code
 * OMSOut omsOut = new OMSOutImplementation();
 * OMSIn oms = new OMSImpl(omsOut);
 *
 * NewOrderSingle newOrder = new NewOrderSingle()
 *     .sender("ClientA")
 *     .target("OMS")
 *     .clOrdID("Order123")
 *     .symbol("AAPL")
 *     .orderQty(100)
 *     .price(150.00)
 *     .side(Side.BUY)
 *     .ordType(OrderType.MARKET)
 *     .transactTime(System.currentTimeMillis());
 *
 * oms.newOrderSingle(newOrder);
 * }</pre>
 */
public class OMSImpl implements OMSIn {
    // The outbound interface for sending execution reports and order cancel rejections
    private final OMSOut out;

    // Reusable instance of ExecutionReport for creating new orders
    private final ExecutionReport er = new ExecutionReport();

    // Reusable instance of OrderCancelReject for cancelling orders
    private final OrderCancelReject ocr = new OrderCancelReject();

    /**
     * Constructs a new {@code OMSImpl} with a given outbound interface.
     *
     * @param out the outbound interface to be used for sending responses
     */
    public OMSImpl(OMSOut out) {
        this.out = out;
    }

    /**
     * Processes a new single order request. The request's details are populated into an execution report and sent out.
     *
     * @param nos the {@link NewOrderSingle} request to process
     */
    @Override
    public void newOrderSingle(NewOrderSingle nos) {
        // Reset the execution report DTO
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
                .cumQty(0)
                .avgPx(0)
                .orderID(orderID)
                .text("Not ready");

        // Send execution report
        out.executionReport(er);
    }

    /**
     * Processes a cancel order request. The request's details are populated into an order cancel rejection and sent out.
     *
     * @param cor the {@link CancelOrderRequest} request to process
     */
    @Override
    public void cancelOrderRequest(CancelOrderRequest cor) {
        // Reset the reusable OrderCancelReject instance
        ocr.reset();
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
     * Processes a cancel all orders request. The request's details are populated into an order cancel rejection and sent out.
     *
     * @param cancelAll the {@link CancelAll} request to process
     */
    @Override
    public void cancelAll(CancelAll cancelAll) {
        // Reset the reusable OrderCancelReject instance
        ocr.reset();
        // Populate OrderCancelReject with request details
        ocr.sender(cancelAll.target())
                .target(cancelAll.sender())
                .symbol(cancelAll.symbol())
                .clOrdID(cancelAll.clOrdID())
                .sendingTime(cancelAll.sendingTime())
                .reason("No orders to cancel");

        // Send order cancellation rejection
        out.orderCancelReject(ocr);
    }
}
