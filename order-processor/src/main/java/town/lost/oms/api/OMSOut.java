/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.api;

import town.lost.oms.dto.ExecutionReport;
import town.lost.oms.dto.OrderCancelReject;

public interface OMSOut {
    void executionReport(ExecutionReport er);

    void orderCancelReject(OrderCancelReject ocr);
}
