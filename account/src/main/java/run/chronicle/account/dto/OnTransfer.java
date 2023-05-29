/*
 * Copyright 2016-2022 chronicle.software
 *
 *       https://chronicle.software
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
 * The OnTransfer class extends AbstractEvent and represents an event that's triggered when a Transfer action takes place.
 * It encapsulates a reference to the Transfer instance that initiated this event.
 * This class follows the convention of using a fluent interface for setters,
 * and it includes a validate method that checks if the transfer field has been properly set.
 */
public class OnTransfer extends AbstractEvent<OnTransfer> {
    private Transfer transfer; // The Transfer instance that triggered this event

    /**
     * @return the Transfer instance that triggered this event
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
        super.validate(); // Validate fields in the parent class
        if (transfer == null)
            throw new InvalidMarshallableException("transfer must be set"); // Ensure transfer is set
    }

    /**
     * Overridden to specify the message format. In this case, it uses a lower level binary format,
     * not a self-describing message.
     *
     * @return false as it does not use a self-describing message.
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        // use a lower level binary format, not a self-describing message
        return false;
    }
}
