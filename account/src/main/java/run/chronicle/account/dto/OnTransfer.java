//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;

/**
 * Represents an event indicating that a funds transfer has taken place.
 * This class extends {@link AbstractEvent}, adding a reference to the
 * {@link Transfer} instance that initiated the event.
 *
 * <p>The class follows a fluent interface pattern for setter methods:
 * <pre>{@code
 * OnTransfer event = new OnTransfer()
 *     .sender(vaultId)
 *     .target(gatewayId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .transfer(transferRequest);
 * }</pre>
 */
public class OnTransfer extends AbstractEvent<OnTransfer> {
    private Transfer transfer;

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
     * Validates that all required properties have been set and are valid. This includes
     * the fields inherited from {@link AbstractEvent} (sender, target, sendingTime) and
     * the {@code Transfer} instance itself.
     *
     * @throws InvalidMarshallableException If any of these properties is not set
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the superclass

        if (transfer == null) {
            throw new InvalidMarshallableException("Invalid OnTransfer event: 'transfer' must not be null.");
        }
        transfer.validate();
    }

    /**
     * Indicates that this event does not use a self-describing message format and instead
     * relies on a more compact binary representation.
     *
     * @return {@code false} as this event does not use a self-describing message format
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        // use a lower level binary format, not a self-describing message
        return false;
    }
}
