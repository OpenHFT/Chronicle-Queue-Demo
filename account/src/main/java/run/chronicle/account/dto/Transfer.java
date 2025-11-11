/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.dto;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.converter.ShortText;

/**
 * Represents a funds transfer operation between two accounts.
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * Transfer tx = new Transfer()
 *     .sender(vaultId)
 *     .target(gatewayId)
 *     .sendingTime(SystemTimeProvider.CLOCK.currentTimeNanos())
 *     .from(101013L)
 *     .to(101025L)
 *     .currency(EUR_CODE)
 *     .amount(10.0)
 *     .reference(Bytes.from("Payment for services"));
 * }</pre>
 */
public class Transfer extends AbstractEvent<Transfer> {
    private long from, to;
    @ShortText
    private int currency;
    private double amount;
    private final Bytes<?> reference = Bytes.allocateElasticOnHeap();

    /**
     * Retrieves the sender's account number.
     *
     * @return the sender's account number
     */
    public long from() {
        return from;
    }

    /**
     * Sets the sender's account number.
     *
     * @param from the sender's account number
     * @return this object for method chaining
     */
    public Transfer from(long from) {
        this.from = from;
        return this;
    }

    /**
     * Retrieves the receiver's account number.
     *
     * @return the receiver's account number
     */
    public long to() {
        return to;
    }

    /**
     * Sets the receiver's account number.
     *
     * @param to the receiver's account number
     * @return this object for method chaining
     */
    public Transfer to(long to) {
        this.to = to;
        return this;
    }

    /**
     * Retrieves the currency code of the transfer.
     *
     * @return the currency code
     */
    public int currency() {
        return currency;
    }

    /**
     * Sets the currency code of the transfer and returns this instance.
     * The currency code should typically map to a known currency.
     *
     * @param currency the currency code to set (e.g., "EUR", "USD")
     * @return this object for method chaining
     */
    public Transfer currency(int currency) {
        this.currency = currency;
        return this;
    }

    /**
     * Retrieves the amount to be transferred.
     *
     * @return the amount to be transferred
     */
    public double amount() {
        return amount;
    }

    /**
     * Sets the amount to be transferred.
     *
     * @param amount the amount to set
     * @return this object for method chaining
     */
    public Transfer amount(double amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Returns the reference data associated with this transfer.
     * This could be a note, a reference number, or any additional context.
     *
     * @return the transaction reference
     */
    public Bytes<?> reference() {
        return reference;
    }

    /**
     * Sets the reference details for this transfer and returns this instance.
     * The provided {@code Bytes} data is appended after clearing the existing reference.
     *
     * @param reference the reference to the transaction details
     * @return the updated object
     */
    public Transfer reference(Bytes<?> reference) {
        this.reference.clear().append(reference);
        return this;
    }

    /**
     * Validates that all required fields have been set and are valid.
     * This includes:
     * <ul>
     *   <li><strong>from:</strong> must be nonzero</li>
     *   <li><strong>to:</strong> must be nonzero</li>
     *   <li><strong>currency:</strong> must be nonzero</li>
     *   <li><strong>amount:</strong> must be positive</li>
     *   <li><strong>reference:</strong> must be non-null and non-empty</li>
     * </ul>
     *
     * @throws InvalidMarshallableException if any validation check fails
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate(); // Validate fields in the parent class
        if (from == 0)
            throw new InvalidMarshallableException("from must be set"); // Ensure 'from' is set
        if (to == 0)
            throw new InvalidMarshallableException("to must be set"); // Ensure 'to' is set
        if (currency == 0)
            throw new InvalidMarshallableException("currency must be set"); // Ensure 'currency' is set
        if (!(amount > 0))
            throw new InvalidMarshallableException("amount must be positive"); // Ensure 'amount' is positive
        if (reference == null || reference.isEmpty())
            throw new InvalidMarshallableException("reference must be set"); // Ensure 'reference' is set
    }

    /**
     * Specifies that this event uses a lower-level binary format rather than a self-describing message.
     *
     * @return {@code false}, indicating a non-self-describing message format
     */
    @Override
    public boolean usesSelfDescribingMessage() {
        // Use a lower-level binary format, not a self-describing message
        return false;
    }
}
