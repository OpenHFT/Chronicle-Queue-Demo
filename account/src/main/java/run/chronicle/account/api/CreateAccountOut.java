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
