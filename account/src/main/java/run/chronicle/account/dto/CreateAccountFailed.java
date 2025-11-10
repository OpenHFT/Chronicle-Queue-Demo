//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

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
 * Represents an event indicating that an attempt to create an account has failed.
 * This event includes:
 * <ul>
 *   <li><strong>createAccount</strong>: The original {@link CreateAccount} request that failed.</li>
 *   <li><strong>reason</strong>: A descriptive message explaining why the creation failed.</li>
 * </ul>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * CreateAccountFailed caf = new CreateAccountFailed()
 *     .sender(vaultId)
 *     .target(gwId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .createAccount(originalRequest)
 *     .reason("account already exists");
 * }</pre>
 */
public class CreateAccountFailed extends AbstractEvent<CreateAccountFailed> {

    private CreateAccount createAccount;
    private String reason;

    /**
     * @return the CreateAccount instance that failed
     */
    public CreateAccount createAccount() {
        return createAccount;
    }

    /**
     * Sets the {@link CreateAccount} instance that failed.
     *
     * @param createAccount the failed {@code CreateAccount} instance to set
     * @return this object for method chaining
     */
    public CreateAccountFailed createAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
        return this;
    }

    /**
     * Retrieves the reason for the failure.
     *
     * @return the reason for the failure
     */
    public String reason() {
        return reason;
    }

    /**
     * Sets the reason for the failure.
     *
     * @param reason the reason for the failure to set
     * @return this object for method chaining
     */
    public CreateAccountFailed reason(String reason) {
        this.reason = reason;
        return this;
    }

    /**
     * Validates that all necessary properties have been set and are valid.
     * Ensures that the original {@link CreateAccount} request and the reason are present
     * and valid. If validation fails, an {@link InvalidMarshallableException} is thrown.
     *
     * @throws InvalidMarshallableException if validation fails
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the superclass

        if (createAccount == null) {
            throw new InvalidMarshallableException("CreateAccount must be set");
        } else {
            createAccount.validate(); // Validate the CreateAccount instance
        }

        if (reason == null || reason.trim().isEmpty()) {
            throw new InvalidMarshallableException("Reason must be set and not empty");
        }
    }
}
