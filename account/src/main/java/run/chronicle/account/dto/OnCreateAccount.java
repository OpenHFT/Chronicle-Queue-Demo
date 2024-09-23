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
 * The class OnCreateAccount is an extension of the AbstractEvent class,
 * and it represents an event that occurs when a CreateAccount action has successfully occurred.
 * The class contains a reference to the CreateAccount instance that initiated the event.
 * This class follows the convention of using a fluent style for its setters,
 * and it also includes a validate method to make sure that the createAccount field has been properly set.
 */
public class OnCreateAccount extends AbstractEvent<OnCreateAccount> {
    private CreateAccount createAccount; // The CreateAccount instance that triggered this event

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
     * Validates that all necessary properties have been set and are valid.
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
    }
}
