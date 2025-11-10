//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.routing.inout.api;

/**
 * An interface for specifying a route ("via") and obtaining
 * an output destination ("out").
 *
 * Typically, you'd call via("someName") to define a route,
 * then out() to get a ValueMessage for writing messages.
 *
 * @param <V> the type returned by the {@code via} method
 * @param <O> the type returned by the {@code out} method
 */
public interface ViaOut<V, O> {

    /**
     * This method is expected to be implemented to provide a specific route or path.
     *
     * @param name the name of the route
     * @return an instance of type {@code V} representing the specified route
     */
    V via(String name);

    /**
     * This method is expected to be implemented to provide an output destination.
     *
     * @return an instance of type {@code O} representing the output destination
     */
    O out();
}

