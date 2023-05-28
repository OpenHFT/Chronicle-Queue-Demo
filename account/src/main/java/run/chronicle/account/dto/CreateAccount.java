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
import net.openhft.chronicle.wire.converter.Base85;

/**
 * Represents a specific kind of event: the creation of an account.
 * This class extends the abstract event class and adds properties specific to the creation of an account:
 * name, account, currency, and balance. The setters follow a fluent style.
 */
public class CreateAccount extends AbstractEvent<CreateAccount> {
    private String name; // Name associated with the account
    private long account; // Account identifier
    @Base85
    private int currency; // Currency for the account, represented in Base85
    private double balance; // Balance of the account

    /**
     * @return the account identifier
     */
    public long account() {
        return account;
    }

    /**
     * Sets the account identifier and returns the updated object.
     *
     * @param account the account identifier to set
     * @return the updated object
     */
    public CreateAccount account(long account) {
        this.account = account;
        return this;
    }

    /**
     * @return the name of the account
     */
    public String name() {
        return name;
    }

    /**
     * Sets the name and returns the updated object.
     *
     * @param name the name to set
     * @return the updated object
     */
    public CreateAccount name(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the currency
     */
    public int currency() {
        return currency;
    }

    /**
     * Sets the currency and returns the updated object.
     *
     * @param currency the currency to set
     * @return the updated object
     */
    public CreateAccount currency(int currency) {
        this.currency = currency;
        return this;
    }

    /**
     * @return the balance
     */
    public double balance() {
        return balance;
    }

    /**
     * Sets the balance and returns the updated object.
     *
     * @param balance the balance to set
     * @return the updated object
     */
    public CreateAccount balance(double balance) {
        this.balance = balance;
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
        if (name == null)
            throw new InvalidMarshallableException("name must be set"); // Ensure name is set
        if (account == 0)
            throw new InvalidMarshallableException("account must be set"); // Ensure account is set
        if (currency == 0)
            throw new InvalidMarshallableException("currency must be set"); // Ensure currency is set
        if (balance == 0)
            throw new InvalidMarshallableException("balance must be set"); // Ensure balance is set
    }
}
