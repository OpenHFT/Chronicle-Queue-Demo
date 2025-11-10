//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account.impl;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import run.chronicle.account.api.AccountManagerIn;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.domain.AccountService;
import run.chronicle.account.dto.*;

import static net.openhft.chronicle.core.time.SystemTimeProvider.CLOCK;

/**
 * This class now primarily orchestrates the handling of events, delegating all
 * domain logic (account validation, fund transfers, currency checks) to the
 * AccountService.
 */
public class AccountManagerImpl extends SelfDescribingMarshallable implements AccountManagerIn {
    private final AccountManagerOut out;
    private final AccountService accountService;

    // Reusable event objects
    private final OnCreateAccount onCreateAccount = new OnCreateAccount();
    private final CreateAccountFailed createAccountFailed = new CreateAccountFailed();
    private final OnTransfer onTransfer = new OnTransfer();
    private final TransferFailed transferFailed = new TransferFailed();

    private long id;

    public AccountManagerImpl(AccountManagerOut out) {
        this(out, new AccountService());
    }

    public AccountManagerImpl(AccountManagerOut out, AccountService accountService) {
        this.out = out;
        this.accountService = accountService;
    }

    public AccountManagerImpl id(long id) {
        this.id = id;
        return this;
    }

    @Override
    public void createAccount(CreateAccount createAccount) throws InvalidMarshallableException {
        String failureReason = accountService.tryCreateAccount(createAccount, id);
        if (failureReason == null) {
            sendOnCreateAccount(createAccount);
        } else {
            sendCreateAccountFailed(createAccount, failureReason);
        }
    }

    @Override
    public void transfer(Transfer transfer) {
        try {
            String failureReason = accountService.tryTransfer(transfer, id);
            if (failureReason == null) {
                sendOnTransfer(transfer);
            } else {
                sendTransferFailed(transfer, failureReason);
            }
        } catch (InvalidMarshallableException e) {
            sendTransferFailed(transfer, e.getMessage());
        }
    }

    @Override
    public void checkPoint(CheckPoint checkPoint) {
        if (checkPoint.target() != id) {
            // Ignoring checkpoint as target does not match
            return;
        }

        out.startCheckpoint(checkPoint);
        for (CreateAccount ca : accountService.getAllAccounts().values()) {
            sendOnCreateAccount(ca);
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
