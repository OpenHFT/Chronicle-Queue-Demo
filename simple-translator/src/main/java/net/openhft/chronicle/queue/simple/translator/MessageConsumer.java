/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.translator;

/**
 * Represents a consumer that processes translated messages.
 * <p>
 * Implementations of this interface define the strategy for handling or processing
 * messages that have been translated from one language to another.
 * </p>
 *
 * Created by catherine on 26/07/2016.
 */
@FunctionalInterface
public interface MessageConsumer {

    /**
     * Processes the specified message.
     *
     * @param message the translated message to be processed; must not be {@code null}
     * @throws IllegalArgumentException if {@code message} is {@code null}
     */
    void onMessage(String message);
}
