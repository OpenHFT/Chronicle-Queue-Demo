/*
 * Copyright 2016-2025 chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
 * @param <E> the type of the event extending {@code AbstractEvent}
 */
@SuppressWarnings("unchecked")
public abstract class AbstractEvent<E extends AbstractEvent<E>>
        extends SelfDescribingMarshallable
        implements Validatable {
    @ShortText
    private long sender;  // sender represented in ShortText

    @ShortText
    private long target;  // target represented in ShortText

    @NanoTime
    private long sendingTime;  // sendingTime represented as a unique timestamp in nanoseconds

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
     * Retrieves the sending time.
     *
     * @return the sending time in nanoseconds
     */
    public long sendingTime() {
        return sendingTime;
    }

    /**
     * Sets the sending time and returns the updated object.
     *
     * @param sendingTime the sending time to set
     * @return the updated object
     */
    public E sendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
        return (E) this;
    }

    /**
     * The validate method is used to verify that all necessary properties have been set.
     *
     * @throws InvalidMarshallableException If any of these properties is not set
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        if (sender == 0)
            throw new InvalidMarshallableException("sender must be set");  // ensure sender is set
        if (target == 0)
            throw new InvalidMarshallableException("target must be set");  // ensure target is set
        if (sendingTime == 0)
            throw new InvalidMarshallableException("sendingTime must be set");  // ensure sendingTime is set
    }
}
