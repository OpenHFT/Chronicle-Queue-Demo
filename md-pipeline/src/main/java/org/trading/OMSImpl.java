/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import org.trading.api.OMSIn;
import org.trading.api.OMSOut;
import org.trading.dto.NewOrderSingle;

public class OMSImpl implements OMSIn {
    private final OMSOut out;

    public OMSImpl(OMSOut out) {
        this.out = out;
    }

    public static void main(String[] args) {
        Runner.run("strat-out", "oms-out", OMSOut.class, OMSImpl::new);
    }

    @Override
    public void newOrderSingle(NewOrderSingle nos) {
        System.out.println("I got an order! " + nos);
    }
}
