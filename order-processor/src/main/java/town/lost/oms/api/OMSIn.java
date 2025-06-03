/*
 * Copyright 2016-2025 chronicle.software
 */

package town.lost.oms.api;

import net.openhft.chronicle.bytes.MethodId;
import town.lost.oms.dto.CancelAll;
import town.lost.oms.dto.CancelOrderRequest;
import town.lost.oms.dto.NewOrderSingle;

/**
 * The {@code OMSIn} interface defines the operations that can be performed by an Order Management System (OMS).
 * <p>
 * It includes methods to handle new single orders, cancel order requests, and cancel all orders.
 * Each method receives an instance of a data transfer object that represents the details of the operation.
 *
 * @see NewOrderSingle
 * @see CancelOrderRequest
 * @see CancelAll
 */
public interface OMSIn {

    /**
     * Handles a new single order submission.
     *
     * @param nos The {@link NewOrderSingle} object representing the details of the new order.
     */
    @MethodId(1)
    void newOrderSingle(NewOrderSingle nos);

    /**
     * Handles a cancel order request.
     *
     * @param cor The {@link CancelOrderRequest} object representing the details of the cancel order request.
     */
    @MethodId(2)
    void cancelOrderRequest(CancelOrderRequest cor);

    /**
     * Handles a cancel all orders request.
     *
     * @param cancelAll The {@link CancelAll} object representing the details of the cancel all orders request.
     */
    void cancelAll(CancelAll cancelAll);
}
