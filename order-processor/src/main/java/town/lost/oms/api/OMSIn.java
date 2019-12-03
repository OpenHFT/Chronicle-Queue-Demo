/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.api;

import town.lost.oms.dto.CancelOrderRequest;
import town.lost.oms.dto.NewOrderSingle;

public interface OMSIn {
    void newOrderSingle(NewOrderSingle nos);

    void cancelOrderRequest(CancelOrderRequest cor);
}
