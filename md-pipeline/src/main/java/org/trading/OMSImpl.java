/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import org.trading.api.OMSIn;
import org.trading.api.OMSOut;
import org.trading.dto.NewOrderSingle;

/**
 * This is the OMSImpl class, which implements the OMSIn interface.
 * It represents the core functionality of an Order Management System (OMS) and provides the methods for interacting with the OMS.
 */
public class OMSImpl implements OMSIn {

    // This is the output interface for the OMS
    private final OMSOut out;

    /**
     * This is the constructor for the OMSImpl class.
     * It initializes the out instance variable with the provided OMSOut object.
     *
     * @param out An instance of the OMSOut interface that will handle the output of the OMS.
     */
    public OMSImpl(OMSOut out) {
        this.out = out;
    }

    /**
     * This is the main method for the OMSImpl class.
     * It sets up the system and initializes the necessary components for running the OMS.
     *
     * @param args Command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        // Running the system with the specified input and output channels and the provided OMSOut class
        Runner.run("strat-out", "oms-out", OMSOut.class, OMSImpl::new);
    }

    @Override
    public void newOrderSingle(NewOrderSingle nos) {
        System.out.println("I got an order! " + nos);
    }
}
