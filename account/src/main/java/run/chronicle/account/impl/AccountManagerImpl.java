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

package run.chronicle.account.impl;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.dto.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

/**
 * AccountManagerImpl is the implementation of the AccountManagerIn interface.
 * It handles account creation, money transfers and checkpoints in the account management system.
 */
public class AccountManagerImpl
        extends SelfDescribingMarshallable
        implements AccountManagerIn {
    private transient final AccountManagerOut out;
    // use a primitive long map
    private final Map<Long, CreateAccount> accountsMap = new LinkedHashMap<>();
    // DTOs for events out
    private final OnCreateAccount onCreateAccount = new OnCreateAccount();
    private final CreateAccountFailed createAccountFailed = new CreateAccountFailed();
    private final OnTransfer onTransfer = new OnTransfer();
    private final TransferFailed transferFailed = new TransferFailed();
    private long id;

    /**
     * The constructor for the AccountManagerImpl class.
     *
     * @param out An instance of AccountManagerOut, which handles output events.
     */
    public AccountManagerImpl(AccountManagerOut out) {
        this.out = out;
    }

    /**
     * Sets the id of the AccountManagerImpl instance.
     *
     * @param id A long representing the id to be set.
     * @return This AccountManagerImpl instance.
     */
    public AccountManagerImpl id(long id) {
        this.id = id;
        return this;
    }

    /**
     * Handles account creation.
     *
     * @param createAccount An instance of CreateAccount containing details of the account to be created.
     * @throws InvalidMarshallableException If there's an error during the process.
     */
    @Override
    public void createAccount(CreateAccount createAccount) throws InvalidMarshallableException {
        // Verify if the account creation request is intended for this instance by checking the target of the request against the id of this instance
        // If they don't match, a failure message is sent with the reason "target mismatch" and the method returns
        if (createAccount.target() != id) {
            sendCreateAccountFailed(createAccount, "target mismatch");
            return;
        }

        // Verify if the initial balance for the account is greater than or equal to 0
        // If it isn't, a failure message is sent with the reason "invalid balance" and the method returns
        if (!(createAccount.balance() >= 0)) {
            sendCreateAccountFailed(createAccount, "invalid balance");
            return;
        }

        // Get the account number for the new account
        Long account = createAccount.account();

        // Check if the account already exists in the accounts map
        // If it does, a failure message is sent with the reason "account already exists" and the method returns
        if (accountsMap.containsKey(account)) {
            sendCreateAccountFailed(createAccount, "account already exists");
            return;
        }

        // If all checks pass, create a copy of the CreateAccount object and add it to the accounts map
        // This is to ensure we retain a version of the data, even if the original CreateAccount object changes later
        accountsMap.put(account, createAccount.deepCopy());

        // Send a confirmation message indicating the account was successfully created
        sendOnCreateAccount(createAccount);
    }


    /**
     * Handles transfers between accounts.
     *
     * @param transfer An instance of Transfer containing details of the transfer to be performed.
     */
    @Override
    public void transfer(Transfer transfer) {
        // Verify if the transfer is intended for this instance by checking the target of the transfer against the id of this instance
        // If it doesn't match, a failure message is sent with the reason "target mismatch" and the method returns
        if (transfer.target() != id) {
            sendTransferFailed(transfer, "target mismatch");
            return;
        }

        // Get the account from which funds are to be transferred
        // If it doesn't exist, a failure message is sent with the reason "from account doesn't exist" and the method returns
        CreateAccount fromAccount = accountsMap.get(transfer.from());
        if (fromAccount == null) {
            sendTransferFailed(transfer, "from account doesn't exist");
            return;
        }

        // Check if the currency of the transfer matches the currency of the "from" account
        // If they don't match, a failure message is sent with the reason "from account currency doesn't match" and the method returns
        if (fromAccount.currency() != transfer.currency()) {
            sendTransferFailed(transfer, "from account currency doesn't match");
            return;
        }

        double amount = transfer.amount();

        // Check if the balance of the "from" account is sufficient to perform the transfer
        // If it isn't, a failure message is sent with the reason "insufficient funds" and the method returns
        if (fromAccount.balance() + fromAccount.overdraft() < amount) {
            sendTransferFailed(transfer, "insufficient funds");
            return;
        }

        // Get the account to which funds are to be transferred
        // If it doesn't exist, a failure message is sent with the reason "to account doesn't exist" and the method returns
        CreateAccount toAccount = accountsMap.get(transfer.to());
        if (toAccount == null) {
            sendTransferFailed(transfer, "to account doesn't exist");
            return;
        }

        // Check if the currency of the transfer matches the currency of the "to" account
        // If they don't match, a failure message is sent with the reason "to account currency doesn't match" and the method returns
        if (toAccount.currency() != transfer.currency()) {
            sendTransferFailed(transfer, "to account currency doesn't match");
            return;
        }

        // Perform the transfer: deduct the amount from the "from" account and add it to the "to" account
        fromAccount.balance(fromAccount.balance() - amount);
        toAccount.balance(toAccount.balance() + amount);

        // Send a confirmation message indicating the transfer was successful
        sendOnTransfer(transfer);
    }

    /**
     * Handles checkpointing in the account management system.
     *
     * @param checkPoint An instance of CheckPoint which provides details of the checkpoint.
     */
    @Override
    public void checkPoint(CheckPoint checkPoint) {
        // Check if the checkpoint target matches the ID of this instance. If it does not match, ignore this checkpoint.
        if (checkPoint.target() != id)
            return; // ignored

        // Start the checkpoint process. This is typically when we serialize and store the state of the system
        // to an output stream (out). CheckPoint instance contains details about the checkpoint.
        out.startCheckpoint(checkPoint);

        // Iterate over all accounts in the accountsMap. For each account, send a "create account" event
        // This essentially stores the current state of each account to the output stream. This is useful in case
        // we need to restore the state of the system from this checkpoint in the future.
        for (CreateAccount createAccount : accountsMap.values()) {
            sendOnCreateAccount(createAccount);
        }

        // End the checkpoint process. This is typically when we complete the serialization of the system state
        // and finalize the checkpoint data in the output stream.
        out.endCheckpoint(checkPoint);
    }

    private void sendCreateAccountFailed(CreateAccount createAccount, String reason) {
        out.createAccountFailed(createAccountFailed
                .sender(id)
                .target(createAccount.sender())
                .sendingTime(CLOCK.currentTimeNanos())
                .createAccount(createAccount)
                .reason(reason));
    }

    private void sendOnCreateAccount(CreateAccount createAccount) {
        out.onCreateAccount(onCreateAccount
                .sender(id)
                .target(createAccount.sender())
                .sendingTime(CLOCK.currentTimeNanos())
                .createAccount(createAccount));
    }

    private void sendTransferFailed(Transfer transfer, String reason) {
        out.transferFailed(transferFailed
                .sender(id)
                .target(transfer.sender())
                .sendingTime(CLOCK.currentTimeNanos())
                .transfer(transfer)
                .reason(reason));
    }

    private void sendOnTransfer(Transfer transfer) {
        out.onTransfer(onTransfer
                .sender(id)
                .target(transfer.sender())
                .sendingTime(CLOCK.currentTimeNanos())
                .transfer(transfer));
    }
}
