/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.routing.out.api;

/**
 * This is an EvenIn interface.
 * It provides a contract for classes that need to process an Even object.
 * The purpose of this interface is to encapsulate the operation on an Even object.
 */
public interface EvenIn {

    /**
     * This method provides a contract for processing an Even object.
     * Any class implementing this interface will need to provide its own implementation of this method.
     *
     * @param even An Even object to be processed
     */
    void even(Even even);
}
