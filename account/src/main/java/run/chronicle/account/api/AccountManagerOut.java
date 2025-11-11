/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.api;

import run.chronicle.account.dto.CheckPoint;

/**
 * This interface extends both the CreateAccountOut and TransferOut interfaces.
 * It provides methods to manage account operations including account creation,
 * transfers, and checkpoint handling.
 */
public interface AccountManagerOut extends CreateAccountOut, TransferOut {

    /**
     * This method initiates a checkpoint operation.
     *
     * @param checkPoint a CheckPoint object encapsulating the details of the
     *                   checkpoint that started the following state dump.
     */
    void startCheckpoint(CheckPoint checkPoint);

    /**
     * This method concludes a checkpoint operation.
     *
     * @param checkPoint a CheckPoint object encapsulating the details of the
     *                   checkpoint that started the previous state dump.
     */
    void endCheckpoint(CheckPoint checkPoint);
}
