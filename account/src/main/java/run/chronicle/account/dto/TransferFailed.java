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
 * Like other classes, it also follows the Fluent Interface pattern for setters, allowing chaining of method calls.
 */
public class TransferFailed extends AbstractEvent<TransferFailed> {
    private Transfer transfer; // The original transfer that failed
    private String reason; // The reason for the failure

    /**
     * Returns the original transfer that failed.
     *
     * @return the original transfer that failed
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
     * Returns the reason for the failure.
     *
     * @return the reason for the failure
     */
    public String reason() {
        return reason;
    }

    /**
     * Sets the reason for the failure and returns the updated object.
     *
     * @param reason the reason for the failure
     * @return the updated object
     */
    public TransferFailed reason(String reason) {
        this.reason = reason;
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
            throw new InvalidMarshallableException("transfer must be set"); // Ensure 'transfer' is set
        if (reason == null)
            throw new InvalidMarshallableException("reason must be set"); // Ensure 'reason' is set
    }
}
