/*
 * Copyright 2016-2025 chronicle.software
 */

package town.lost.oms.api;

import net.openhft.chronicle.bytes.MethodId;
import town.lost.oms.dto.CancelAll;
import town.lost.oms.dto.CancelOrderRequest;
import town.lost.oms.dto.NewOrderSingle;

/**
 * The {@code OMSIn} interface defines inbound operations for an Order Management System (OMS).
 * <p>
 * This interface is typically called by the event-driven framework after the inbound DTOs have
 * passed validation. Implementors (e.g., {@link town.lost.oms.OMSImpl}) are responsible
 * for generating the appropriate outbound events (e.g., {@code ExecutionReport}, {@code OrderCancelReject})
 * via an {@link OMSOut} reference.
 * <p>
 * Thread-safety: This demo assumes a single-threaded environment.
 */
public interface OMSIn {

    /**
     * Processes a new single-order submission. Typically leads to an {@code ExecutionReport} on success
     * or an {@code OrderCancelReject} on failure (if the order is invalid).
     *
     * @param nos The {@link NewOrderSingle} object representing the details of the new order.
     */
    @MethodId(1)
    void newOrderSingle(NewOrderSingle nos);

    /**
     * Processes a request to cancel a specific existing order. If no matching order is found,
     * the implementation is expected to generate an {@code OrderCancelReject} with a reason like "No such order".
     *
     * @param cor The {@link CancelOrderRequest} object representing the details of the cancel request.
     */
    @MethodId(2)
    void cancelOrderRequest(CancelOrderRequest cor);

    /**
     * Processes a request to cancel all orders matching a specific filter (e.g., by symbol).
     * In a minimal demo scenario, this may reject if no orders exist or simply log the cancellation attempt.
     *
     * @param cancelAll The {@link CancelAll} object representing the details of the mass-cancel request.
     */
    void cancelAll(CancelAll cancelAll);
}
