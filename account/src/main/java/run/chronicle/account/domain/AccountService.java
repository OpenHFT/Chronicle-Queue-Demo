/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.domain;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import run.chronicle.account.dto.CreateAccount;
import run.chronicle.account.dto.Transfer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Encapsulates all domain logic related to account creation, validation, and funds transfers.
 * This service is responsible for:
 * - Managing the in-memory store of accounts.
 * - Validating currency, balances, and overdraft limits.
 * - Updating account balances upon successful transfers.
 */
public class AccountService {

    // In-memory account store
    private final Map<Long, CreateAccount> accountsMap = new LinkedHashMap<>();

    /**
     * Attempts to create a new account. Throws an InvalidMarshallableException if invalid,
     * or returns a reason string if creation should fail due to domain rules.
     */
    public String tryCreateAccount(CreateAccount createAccount, long expectedTargetId) throws InvalidMarshallableException {
        // Validate DTO properties
        createAccount.validate();

        if (createAccount.target() != expectedTargetId) {
            return "target mismatch";
        }

        if (!(createAccount.balance() >= 0)) {
            return "invalid balance";
        }

        Long accountNumber = createAccount.account();
        if (accountsMap.containsKey(accountNumber)) {
            return "account already exists";
        }

        // If valid, store the account
        accountsMap.put(accountNumber, createAccount.deepCopy());
        return null; // Indicates success
    }

    /**
     * Attempts to perform a transfer. Returns a reason string if transfer fails domain checks,
     * or null if the transfer is successful.
     */
    public String tryTransfer(Transfer transfer, long expectedTargetId) throws InvalidMarshallableException {
        // Validate DTO properties
        transfer.validate();

        if (transfer.target() != expectedTargetId) {
            return "target mismatch";
        }

        CreateAccount fromAccount = accountsMap.get(transfer.from());
        if (fromAccount == null) {
            return "from account doesn't exist";
        }
        if (fromAccount.currency() != transfer.currency()) {
            return "from account currency doesn't match";
        }

        double amount = transfer.amount();
        if (fromAccount.balance() + fromAccount.overdraft() < amount) {
            return "insufficient funds";
        }

        CreateAccount toAccount = accountsMap.get(transfer.to());
        if (toAccount == null) {
            return "to account doesn't exist";
        }
        if (toAccount.currency() != transfer.currency()) {
            return "to account currency doesn't match";
        }

        // Perform the transfer
        fromAccount.balance(fromAccount.balance() - amount);
        toAccount.balance(toAccount.balance() + amount);

        return null; // Indicates success
    }

    /**
     * Provides access to all accounts for checkpoint operations.
     */
    public Map<Long, CreateAccount> getAllAccounts() {
        return accountsMap;
    }
}
