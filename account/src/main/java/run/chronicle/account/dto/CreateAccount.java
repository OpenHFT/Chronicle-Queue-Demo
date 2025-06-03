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
import net.openhft.chronicle.wire.converter.ShortText;

/**
 * Represents the event of creating a new account.
 * This class extends {@link AbstractEvent} and adds properties specific to account creation,
 * such as the account holder's name, account number, currency, balance, and overdraft limit.
 * Setters are designed using the fluent interface pattern for method chaining.
 */
public class CreateAccount extends AbstractEvent<CreateAccount> {
    private String name; // Name associated with the account
    private long account; // Account identifier
    @ShortText
    private int currency; // Currency for the account
    private double balance; // Initial Balance of the account
    private double overdraft; // Overdraft limit of the account

    /**
     * Retrieves the account identifier.
     *
     * @return the account identifier
     */
    public long account() {
        return account;
    }

    /**
     * Sets the account identifier.
     *
     * @param account the account identifier to set
     * @return this object for method chaining
     */
    public CreateAccount account(long account) {
        this.account = account;
        return this;
    }

    /**
     * Retrieves the name associated with the account.
     *
     * @return the account holder's name
     */
    public String name() {
        return name;
    }

    /**
     * Sets the name associated with the account.
     *
     * @param name the name to set
     * @return this object for method chaining
     */
    public CreateAccount name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Retrieves the currency code.
     *
     * @return the currency code
     */
    public int currency() {
        return currency;
    }

    /**
     * Sets the currency code.
     *
     * @param currency the currency code to set (e.g., "EUR", "USD")
     * @return this object for method chaining
     */
    public CreateAccount currency(int currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Retrieves the initial balance of the account.
     *
     * @return the account balance
     */
    public double balance() {
        return balance;
    }

    /**
     * Sets the initial balance of the account.
     *
     * @param balance the balance to set
     * @return this object for method chaining
     */
    public CreateAccount balance(double balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Retrieves the overdraft limit of the account.
     *
     * @return the overdraft limit
     */
    public double overdraft() {
        return overdraft;
    }

    /**
     * Sets the overdraft limit of the account.
     *
     * @param overdraft the overdraft limit to set
     * @return this object for method chaining
     */
    public CreateAccount overdraft(double overdraft) {
        this.overdraft = overdraft;
        return this;
    }

    /**
     * Validates that all necessary properties have been set and are valid.
     *
     * @throws InvalidMarshallableException if validation fails
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the parent class
        if (name == null || name.trim().isEmpty())
            throw new InvalidMarshallableException("name must be set and not empty"); // Ensure name is set
        if (account == 0)
            throw new InvalidMarshallableException("account must be set"); // Ensure account is set
        if (currency == 0)
            throw new InvalidMarshallableException("currency must be set"); // Ensure currency is set
        if (balance < 0)
            throw new InvalidMarshallableException("balance must be positive or zero"); // Ensure balance is >= 0
        if (overdraft < 0)
            throw new InvalidMarshallableException("overdraft must be positive or zero"); // Ensure overdraft is >= 0
    }
}
