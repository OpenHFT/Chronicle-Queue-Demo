/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.api;

import run.chronicle.account.dto.CreateAccountFailed;
import run.chronicle.account.dto.OnCreateAccount;
import run.chronicle.account.util.ErrorListener;

/**
 * This interface extends the ErrorListener interface. It is used to define methods related to
 * account creation. This includes notifying about the creation of an account and handling failures
 * during account creation.
 */
public interface CreateAccountOut extends ErrorListener {

    /**
     * Method to notify about the creation of an account.
     *
     * @param onCreateAccount an object of type OnCreateAccount which encapsulates the details of
     *                        the created account.
     */
    void onCreateAccount(OnCreateAccount onCreateAccount);

    /**
     * Method to handle failures during account creation.
     *
     * @param createAccountFailed an object of type CreateAccountFailed which encapsulates the
     *                            details of the account creation failure.
     */
    void createAccountFailed(CreateAccountFailed createAccountFailed);
}
