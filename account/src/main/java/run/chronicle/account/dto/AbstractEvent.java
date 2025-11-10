//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.core.io.Validatable;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;

/**
 * The {@code AbstractEvent} class serves as a base for event objects,
 * extending {@link SelfDescribingMarshallable} and implementing {@link Validatable}.
 * It provides common properties like sender, target, and sending time,
 * along with fluent setter methods for easy chaining.
 *
 * <p>Concrete subclasses must ensure that a valid sender, target, and sendingTime
 * are provided before use. Validation throws an {@link InvalidMarshallableException}
 * if any required field is not set.</p>
 *
 * @param <E> the type of the event extending {@code AbstractEvent}
 */
@SuppressWarnings("unchecked")
public abstract class AbstractEvent<E extends AbstractEvent<E>>
        extends SelfDescribingMarshallable
        implements Validatable {

    @ShortText
    private long sender;

    @ShortText
    private long target;

    @NanoTime
    private long sendingTime;

    /**
     * Retrieves the sender identifier.
     *
     * @return the sender identifier
     */
    public long sender() {
        return sender;
    }

    /**
     * Sets the sender and returns the updated object.
     *
     * @param sender the sender to set
     * @return the updated object
     */
    public E sender(long sender) {
        this.sender = sender;
        return (E) this;
    }

    /**
     * Retrieves the target identifier.
     *
     * @return the target identifier
     */
    public long target() {
        return target;
    }

    /**
     * Sets the target and returns the updated object.
     *
     * @param target the target to set
     * @return the updated object
     */
    public E target(long target) {
        this.target = target;
        return (E) this;
    }

    /**
     * Retrieves the sending wall clock time since epoch.
     *
     * @return the sending time in nanoseconds
     */
    public long sendingTime() {
        return sendingTime;
    }

    /**
     * Sets the sending wall clock time since epoch and returns the updated object.
     *
     * @param sendingTime the sending time to set
     * @return the updated object
     */
    public E sendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
        return (E) this;
    }

    /**
     * Validates that all required fields (sender, target, and sendingTime) have been set.
     * If any field is unset (0 indicates an unset value), it throws an {@link InvalidMarshallableException}.
     *
     * @throws InvalidMarshallableException If any of these properties is not set
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        if (sender == 0)
            throw new InvalidMarshallableException("sender must be set");
        if (target == 0)
            throw new InvalidMarshallableException("target must be set");
        if (sendingTime == 0)
            throw new InvalidMarshallableException("sendingTime must be set");
    }
}
