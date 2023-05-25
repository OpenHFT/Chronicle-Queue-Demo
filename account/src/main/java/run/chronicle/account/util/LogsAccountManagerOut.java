package run.chronicle.account.util;

import net.openhft.chronicle.core.Jvm;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.dto.*;

public class LogsAccountManagerOut implements AccountManagerOut {
    @Override
    public void startCheckpoint(CheckPoint checkPoint) {
        Jvm.debug().on(getClass(), "startCheckpoint " + checkPoint);
    }

    @Override
    public void endCheckpoint(CheckPoint checkPoint) {
        Jvm.debug().on(getClass(), "endCheckpoint " + checkPoint);
    }

    @Override
    public void onCreateAccount(OnCreateAccount onCreateAccount) {
        Jvm.debug().on(getClass(), "onCreateAccount " + onCreateAccount);
    }

    @Override
    public void createAccountFailed(CreateAccountFailed createAccountFailed) {
        Jvm.debug().on(getClass(), "createAccountFailed " + createAccountFailed);
    }

    @Override
    public void onTransfer(OnTransfer onTransfer) {
        Jvm.debug().on(getClass(), "onTransfer " + onTransfer);
    }

    @Override
    public void transferFailed(TransferFailed transferFailed) {
        Jvm.debug().on(getClass(), "transferFailed " + transferFailed);
    }

    @Override
    public void jvmError(String msg) {
        Jvm.warn().on(getClass(), "jvmError " + msg);
    }
}
