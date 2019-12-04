/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.api;

import net.openhft.chronicle.bytes.MethodId;
import town.lost.oms.dto.ExecutionReport;
import town.lost.oms.dto.OrderCancelReject;

public interface OMSOut {
    @MethodId(11)
    void executionReport(ExecutionReport er);

    @MethodId(12)
    void orderCancelReject(OrderCancelReject ocr);
}
