//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package town.lost.oms.api;

import net.openhft.chronicle.bytes.MethodId;
import town.lost.oms.dto.ExecutionReport;
import town.lost.oms.dto.OrderCancelReject;

/**
 * The {@code OMSOut} interface defines outbound operations from the Order Management System (OMS).
 * <p>
 * In FIX 4.2 terms, these correspond to:
 * <ul>
 *   <li><strong>ExecutionReport (35=8)</strong> - see {@link ExecutionReport}</li>
 *   <li><strong>OrderCancelReject (35=9)</strong> - see {@link OrderCancelReject}</li>
 * </ul>
 * This interface also extends {@link ErrorListener} to handle critical errors.
 * Typically, these methods are called by {@code OMSImpl} once inbound requests
 * have been processed and an outcome is determined.
 */
public interface OMSOut extends ErrorListener {

    /**
     * Handles an execution report (FIX 4.2 MsgType=35=8), which might indicate a new,
     * partially filled, filled, or canceled order status.
     *
     * @param er The {@link ExecutionReport} object representing the order’s current state.
     */
    @MethodId(11)
    void executionReport(ExecutionReport er);

    /**
     * Handles an order-cancel-reject (FIX 4.2 MsgType=35=9), indicating that a cancellation
     * request could not be honored (e.g., no such order).
     *
     * @param ocr The {@link OrderCancelReject} object representing the reason for rejection.
     */
    @MethodId(12)
    void orderCancelReject(OrderCancelReject ocr);
}
