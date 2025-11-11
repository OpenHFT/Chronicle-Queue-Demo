/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.api;

import net.openhft.chronicle.bytes.MethodId;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import run.chronicle.account.dto.CheckPoint;
import run.chronicle.account.dto.CreateAccount;
import run.chronicle.account.dto.Transfer;

/**
 * The 'AccountManagerIn' interface is the public API for actions that can be taken on accounts
 * It exposes the methods needed for account creation, transferring money, and creating checkpoints
 */
public interface AccountManagerIn {

    /**
     * This method is used to create a new account, passing in the 'CreateAccount' DTO as the argument
     * It throws an 'InvalidMarshallableException' if there is an issue with the data in 'CreateAccount'
     *
     * @param createAccount account to create
     */
    void createAccount(CreateAccount createAccount) throws InvalidMarshallableException;

    /**
     * This method is used to transfer money from one account to another
     * The 'MethodId' annotation is used for mapping methods during the method dispatch process
     * In this case, 't' is the ID assigned to the 'transfer' method
     * It throws an 'InvalidMarshallableException' if there is an issue with the data in 'Transfer'
     *
     * @param transfer between accounts
     */
    @MethodId('t')
    void transfer(Transfer transfer) throws InvalidMarshallableException;

    /**
     * This method is used to create a checkpoint in the system
     * A checkpoint in this context could represent a state of the system at a certain point in time
     *
     * @param checkPoint notify that a check point should be made
     */
    void checkPoint(CheckPoint checkPoint);
}
