/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.api;

import town.lost.oms.dto.CancelOrderRequest;
import town.lost.oms.dto.NewOrderSingle;

public interface OMSIn {
    //    @MethodId(1)
    void newOrderSingle(NewOrderSingle nos);

    //    @MethodId(2)
    void cancelOrderRequest(CancelOrderRequest cor);
}
