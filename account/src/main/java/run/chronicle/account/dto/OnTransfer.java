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

/**
 * Represents an event that occurs when a {@code Transfer} action takes place.
 * This class extends {@link AbstractEvent} and encapsulates a reference to
 * the {@link Transfer} instance that initiated this event.
 * <p>
 * The class follows the Fluent Interface pattern for setter methods,
 * allowing for method chaining.
 */
public class OnTransfer extends AbstractEvent<OnTransfer> {
    private Transfer transfer; // The Transfer instance that triggered this event

    /**
     * Retrieves the {@link Transfer} instance that triggered this event.
     *
     * @return the {@code Transfer} instance
     */
    public Transfer transfer() {
        return transfer;
    }

    /**
     * Sets the Transfer instance that triggered this event and returns the updated object.
     *
     * @param transfer the Transfer instance that triggered this event
     * @return the updated object
     */
    public OnTransfer transfer(Transfer transfer) {
        this.transfer = transfer;
        return this;
    }

    /**
     * The validate method is used to verify that all necessary properties have been set.
     *
     * @throws InvalidMarshallableException If any of these properties is not set
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the superclass

        if (transfer == null) {
            throw new InvalidMarshallableException("Transfer must be set");
        } else {
            transfer.validate(); // Validate the Transfer instance
        }
    }

    /**
     * Overridden to specify the message format. In this case, it uses a lower level binary format,
     * not a self-describing message.
     *
     * @return {@code false} as it does not use a self-describing message
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        // use a lower level binary format, not a self-describing message
        return false;
    }
}
