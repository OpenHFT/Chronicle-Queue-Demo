/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.api;

import net.openhft.chronicle.bytes.MethodId;
import town.lost.oms.dto.ExecutionReport;
import town.lost.oms.dto.OrderCancelReject;

/**
 * The {@code OMSOut} interface defines the output operations that an Order Management System (OMS) can perform.
 * <p>
 * It includes methods to handle execution reports and order cancel rejections.
 * Each method receives an instance of a data transfer object that represents the details of the operation.
 */
public interface OMSOut {

    /**
     * Handles an execution report.
     *
     * @param er The {@link ExecutionReport} object representing the details of the execution report.
     */
    @MethodId(11)
    void executionReport(ExecutionReport er);

    /**
     * Handles an order cancel reject.
     *
     * @param ocr The {@link OrderCancelReject} object representing the details of the order cancel reject.
     */
    @MethodId(12)
    void orderCancelReject(OrderCancelReject ocr);
}
