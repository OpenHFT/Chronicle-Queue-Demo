/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.api;

import org.trading.dto.NewOrderSingle;

public interface OMSIn {
    void newOrderSingle(NewOrderSingle nos);
}
