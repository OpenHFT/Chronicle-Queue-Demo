/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.util;

import net.openhft.chronicle.core.Jvm;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.dto.*;

/**
 * A mock implementation of {@link AccountManagerOut} that logs all received events.
 * <p>
 * This class is useful for testing, demonstration, or diagnostic purposes. It prints
 * events to the console (via {@link Jvm} logging) without performing any real actions.
 * This allows developers to trace the flow of events through the system and verify
 * that commands and results are being produced as expected.
 */
public class LogsAccountManagerOut implements AccountManagerOut {

    /**
     * Logs the start of a checkpoint operation.
     *
     * @param checkPoint The checkpoint command indicating that a state snapshot should begin.
     */
    @Override
    public void startCheckpoint(CheckPoint checkPoint) {
        Jvm.debug().on(getClass(), "startCheckpoint " + checkPoint);
    }

    /**
     * Logs the end of a checkpoint operation.
     *
     * @param checkPoint The checkpoint command indicating that the state snapshot should conclude.
     */
    @Override
    public void endCheckpoint(CheckPoint checkPoint) {
        Jvm.debug().on(getClass(), "endCheckpoint " + checkPoint);
    }

    /**
     * Logs a successful account creation event.
     *
     * @param onCreateAccount The event confirming that an account has been created.
     */
    @Override
    public void onCreateAccount(OnCreateAccount onCreateAccount) {
        Jvm.debug().on(getClass(), "onCreateAccount " + onCreateAccount);
    }

    /**
     * Logs a failed account creation attempt.
     *
     * @param createAccountFailed The event detailing why an account creation failed.
     */
    @Override
    public void createAccountFailed(CreateAccountFailed createAccountFailed) {
        Jvm.warn().on(getClass(), "createAccountFailed " + createAccountFailed);
    }

    /**
     * Logs a successful funds transfer event.
     *
     * @param onTransfer The event confirming that a transfer has completed successfully.
     */
    @Override
    public void onTransfer(OnTransfer onTransfer) {
        Jvm.debug().on(getClass(), "onTransfer " + onTransfer);
    }

    /**
     * Logs a failed transfer attempt.
     *
     * @param transferFailed The event detailing why a funds transfer failed.
     */
    @Override
    public void transferFailed(TransferFailed transferFailed) {
        Jvm.warn().on(getClass(), "transferFailed " + transferFailed);
    }

    /**
     * Logs a critical JVM-level error event.
     *
     * @param msg A descriptive message about the JVM error encountered.
     */
    @Override
    public void jvmError(String msg) {
        Jvm.error().on(getClass(), "jvmError " + msg);
    }
}
