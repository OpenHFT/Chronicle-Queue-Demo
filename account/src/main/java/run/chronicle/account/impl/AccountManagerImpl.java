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

package run.chronicle.account.impl;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.dto.*;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

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
    public AccountManagerImpl(AccountManagerOut out) {
        this.out = out;
    }

    public AccountManagerImpl id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public void createAccount(CreateAccount createAccount) throws InvalidMarshallableException {
        if (createAccount.target() != id) {
            sendCreateAccountFailed(createAccount, "target mismatch");
            return;
        }
        if (!(createAccount.balance() >= 0)) {
            sendCreateAccountFailed(createAccount, "invalid amount");
            return;
        }
        Long account = createAccount.account();
        if (accountsMap.containsKey(account)) {
            sendCreateAccountFailed(createAccount, "account already exists");
            return;
        }
        // must take a copy of any data we want to retain
        accountsMap.put(account, createAccount.deepCopy());
        sendOnCreateAccount(createAccount);
    }

    @Override
    public void transfer(Transfer transfer) {
        if (transfer.target() != id) {
            sendTransferFailed(transfer, "target mismatch");
            return;
        }
        double amount = transfer.amount();
        if (!(amount > 0)) {
            sendTransferFailed(transfer, "invalid amount");
            return;
        }

        CreateAccount fromAccount = accountsMap.get(transfer.from());
        if (fromAccount == null) {
            sendTransferFailed(transfer, "from account doesn't exist");
            return;
        }
        if (fromAccount.currency() != transfer.currency()) {
            sendTransferFailed(transfer, "from account currency doesn't match");
            return;
        }
        if (fromAccount.balance() < amount) {
            sendTransferFailed(transfer, "insufficient funds");
            return;
        }
        CreateAccount toAccount = accountsMap.get(transfer.to());
        if (toAccount == null) {
            sendTransferFailed(transfer, "to account doesn't exist");
            return;
        }
        if (toAccount.currency() != transfer.currency()) {
            sendTransferFailed(transfer, "to account currency doesn't match");
            return;
        }
        // these changes need to be idempotent
        fromAccount.balance(fromAccount.balance() - amount);
        toAccount.balance(toAccount.balance() + amount);
        sendOnTransfer(transfer);
    }

    @Override
    public void checkPoint(CheckPoint checkPoint) {
        if (checkPoint.target() != id)
            return; // ignored

        out.startCheckpoint(checkPoint);
        for (CreateAccount createAccount : accountsMap.values()) {
            sendOnCreateAccount(createAccount);
        }
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
