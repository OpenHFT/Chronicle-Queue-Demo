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
 * This class, CreateAccountFailed, is an extension of AbstractEvent used to represent a situation
 * where an attempt to create an account has failed. This class adds two properties to the event:
 * a reference to the original CreateAccount object that failed, and a reason string describing why
 * the account creation failed. As with other classes in this system, it uses a fluent style of
 * setters, and includes a validate method to ensure all necessary properties have been set.
 */
public class CreateAccountFailed extends AbstractEvent<CreateAccountFailed> {
    private CreateAccount createAccount; // Reference to the CreateAccount instance that failed
    private String reason; // The reason for the failure

    /**
     * @return the CreateAccount instance that failed
     */
    public CreateAccount createAccount() {
        return createAccount;
    }

    /**
     * Sets the CreateAccount instance that failed and returns the updated object.
     *
     * @param createAccount the CreateAccount instance that failed
     * @return the updated object
     */
    public CreateAccountFailed createAccount(CreateAccount createAccount) {
        this.createAccount = createAccount;
        return this;
    }

    /**
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
    public CreateAccountFailed reason(String reason) {
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
        if (createAccount == null)
            throw new InvalidMarshallableException("createAccount must be set"); // Ensure createAccount is set
        if (reason == null)
            throw new InvalidMarshallableException("reason must be set"); // Ensure reason is set
    }
}
