/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
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
