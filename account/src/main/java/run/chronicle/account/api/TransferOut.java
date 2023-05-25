package run.chronicle.account.api;

import net.openhft.chronicle.bytes.MethodId;
import run.chronicle.account.dto.OnTransfer;
import run.chronicle.account.dto.TransferFailed;
import run.chronicle.account.util.ErrorListener;

public interface TransferOut extends ErrorListener {
    @MethodId('T')
    void onTransfer(OnTransfer onTransfer);

    void transferFailed(TransferFailed transferFailed);
}
