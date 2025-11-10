//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

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

import net.openhft.chronicle.bytes.MethodId;
import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.TransferFailed;
import run.chronicle.account.util.ErrorListener;

/**
 * This interface extends the ErrorListener interface. It is used to define methods related to
 * money transfers between accounts. This includes notifying about a successful transfer and
 * handling failures during a transfer.
 */
public interface TransferOut extends ErrorListener {

    /**
     * Method to notify about a successful transfer.
     *
     * @param onTransfer an object of type OnTransfer which encapsulates the details of the
     *                   successful transfer.
     */
    @MethodId('T')
    void onTransfer(OnTransfer onTransfer);

    /**
     * Method to handle failures during a transfer.
     *
     * @param transferFailed an object of type TransferFailed which encapsulates the details of
     *                       the transfer failure.
     */
    void transferFailed(TransferFailed transferFailed);
}
