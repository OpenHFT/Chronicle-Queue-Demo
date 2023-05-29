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
