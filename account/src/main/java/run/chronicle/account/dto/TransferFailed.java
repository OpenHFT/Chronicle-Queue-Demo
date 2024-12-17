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
 * The TransferFailed class is a type of AbstractEvent that represents a failed transfer operation in the system.
 * It contains the Transfer object that failed and the reason for the failure.
 * <p>Example usage:</p>
 * <pre>{@code
 * TransferFailed event = new TransferFailed()
 *     .sender(vaultId)
 *     .target(gatewayId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .transfer(originalTransfer)
 *     .reason("Insufficient funds");
 * }</pre>
 */
public class TransferFailed extends AbstractEvent<TransferFailed> {
    private Transfer transfer;
    private String reason;

    /**
     * Retrieves the original {@link Transfer} that failed.
     *
     * @return the failed {@code Transfer} instance
     */
    public Transfer transfer() {
        return transfer;
    }

    /**
     * Sets the original transfer that failed and returns the updated object.
     *
     * @param transfer the original transfer that failed
     * @return the updated object
     */
    public TransferFailed transfer(Transfer transfer) {
        this.transfer = transfer;
        return this;
    }

    /**
     * Retrieves the reason for the failure.
     *
     * @return the failure reason
     */
    public String reason() {
        return reason;
    }

    /**
     * Sets the reason for the failure.
     *
     * @param reason the failure reason to set
     * @return this object for method chaining
     */
    public TransferFailed reason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Validates that all required fields have been set and are valid. This includes:
     * <ul>
     *   <li>All fields from {@link AbstractEvent} (sender, target, sendingTime)</li>
     *   <li>A non-null {@link Transfer} instance, which is itself validated</li>
     *   <li>A non-null reason string</li>
     * </ul>
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the superclass

        if (transfer == null) {
            throw new InvalidMarshallableException("Invalid TransferFailed: 'transfer' must not be null.");
        }
        transfer.validate(); // Validate the Transfer object

        if (reason == null) {
            throw new InvalidMarshallableException("Invalid TransferFailed: 'reason' must not be null.");
        }
    }
}
