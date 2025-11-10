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
 * Represents an event indicating that an account was successfully created.
 * This event references the original {@link CreateAccount} request that
 * led to the successful creation.
 *
 * <p>Example usage:
 * <pre>{@code
 * OnCreateAccount event = new OnCreateAccount()
 *     .sender(vaultId)
 *     .target(gatewayId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .createAccount(originalRequest);
 * }</pre>
 */
public class OnCreateAccount extends AbstractEvent<OnCreateAccount> {
    private CreateAccount createAccount;

    /**
     * Retrieves the {@link CreateAccount} instance that triggered this event.
     *
     * @return the {@code CreateAccount} instance
     */
    public CreateAccount createAccount() {
        return createAccount;
    }

    /**
     * Sets the {@link CreateAccount} instance that triggered this event.
     *
     * @param createAccount the {@code CreateAccount} instance to set
     * @return this object for method chaining
     */
    public OnCreateAccount createAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
        return this;
    }

    /**
     * Validates that all required properties are set and valid. This includes:
     * <ul>
     *   <li>All fields from the superclass (sender, target, sendingTime)</li>
     *   <li>A non-null {@link CreateAccount} instance</li>
     *   <li>Validation of the {@code CreateAccount} instance itself</li>
     * </ul>
     *
     * @throws InvalidMarshallableException if validation fails for this event
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the superclass

        if (createAccount == null) {
            throw new InvalidMarshallableException("Invalid OnCreateAccount: 'createAccount' must not be null.");
        }
        createAccount.validate();  // Validate the associated CreateAccount object
    }
}
