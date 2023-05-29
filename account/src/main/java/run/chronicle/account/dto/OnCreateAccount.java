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
     * @return the CreateAccount instance that triggered this event
     */
    public CreateAccount createAccount() {
        return createAccount;
    }

    /**
     * Sets the CreateAccount instance that triggered this event and returns the updated object.
     *
     * @param createAccount the CreateAccount instance that triggered this event
     * @return the updated object
     */
    public OnCreateAccount createAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
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
        if (createAccount == null)
            throw new InvalidMarshallableException("createAccount must be set"); // Ensure createAccount is set
    }
}
