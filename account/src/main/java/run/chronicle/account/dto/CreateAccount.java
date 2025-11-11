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
 * Represents an event for creating a new account. This event includes all necessary details
 * about the account to be created, such as:
 * <ul>
 *   <li><strong>name</strong>: The account holder's name.</li>
 *   <li><strong>account</strong>: A unique identifier for this account.</li>
 *   <li><strong>currency</strong>: The currency code for the account (stored as an integer code).</li>
 *   <li><strong>balance</strong>: The initial balance of the account. Must be ≥ 0.</li>
 *   <li><strong>overdraft</strong>: The overdraft limit for the account. Must be ≥ 0.</li>
 * </ul>
 *
 * <p>This class uses a fluent interface style for setter methods, allowing for chaining:
 * <pre>{@code
 * CreateAccount event = new CreateAccount()
 *     .sender(gatewayId)
 *     .target(serviceId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .name("Alice")
 *     .account(101013L)
 *     .currency(EUR_CODE)
 *     .balance(1000.0)
 *     .overdraft(100.0);
 * }</pre>
 */
public class CreateAccount extends AbstractEvent<CreateAccount> {
    private String name;
    private long account;
    @ShortText
    private int currency;
    private double balance;
    private double overdraft;

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
     * Returns the currency code of the account.
     * This is typically an integer code mapping to a currency (e.g., EUR, USD).
     *
     * @return the currency code
     */
    public int currency() {
        return currency;
    }

    /**
     * Sets the currency code and returns this instance.
     * It is expected that the caller uses predefined integer codes for currencies.
     *
     * @param currency the currency code to set (e.g., "EUR", "USD")
     * @return this instance for method chaining
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
     * Sets the initial balance of the account and returns this instance.
     * The balance must be ≥ 0.
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
     * Sets the overdraft limit of the account and returns this instance.
     * The overdraft limit must be ≥ 0.
     *
     * @param overdraft the overdraft limit to set
     * @return this object for method chaining
     */
    public CreateAccount overdraft(double overdraft) {
        this.overdraft = overdraft;
        return this;
    }

    /**
     * Validates that all required properties (sender, target, sendingTime, name, account, currency, balance, overdraft)
     * have been set correctly.
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
