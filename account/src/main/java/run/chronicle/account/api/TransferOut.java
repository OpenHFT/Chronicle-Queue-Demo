package run.chronicle.account.api;

import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.TransferFailed;
import run.chronicle.account.util.ErrorListener;

public interface TransferOut extends ErrorListener {
    void onTransfer(OnTransfer onTransfer);

    void transferFailed(TransferFailed transferFailed);
}
