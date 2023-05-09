package run.chronicle.account.api;

import run.chronicle.account.dto.CreateAccountFailed;
import run.chronicle.account.dto.OnCreateAccount;
import run.chronicle.account.util.ErrorListener;

public interface CreateAccountOut extends ErrorListener {
    void onCreateAccount(OnCreateAccount onCreateAccount);

    void createAccountFailed(CreateAccountFailed createAccountFailed);
}
