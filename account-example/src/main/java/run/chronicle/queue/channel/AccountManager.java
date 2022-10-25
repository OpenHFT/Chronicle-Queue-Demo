/*
 * Copyright 2016-2022 chronicle.software
 *
 *       https://chronicle.software
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

package run.chronicle.queue.channel;

import run.chronicle.queue.channel.api.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class AccountManager implements AccountManagerIn {
    private final Map<Long, Double> accountBalanceMap = new LinkedHashMap<>();
    private AccountManagerOut out;
    private OnCreateAccount onCreateAccount = new OnCreateAccount();
    private OnTransfer onTransfer = new OnTransfer();

    public AccountManager(AccountManagerOut out) {
        this.out = out;
    }

    @Override
    public void createAccount(CreateAccount createAccount) {
        onCreateAccount.reset();
        createAccount.copyTo(onCreateAccount);
        if (accountBalanceMap.containsKey(createAccount.name())) {
            out.OnCreateAccount(onCreateAccount.success(false).reason("Already exists"));
            return;
        }
        if (createAccount.balance() >= 0) {
            accountBalanceMap.put(createAccount.name(), createAccount.balance());
            out.OnCreateAccount(onCreateAccount.success(true).reason(""));
            return;
        }
        out.OnCreateAccount(onCreateAccount.success(false).reason("Invalid balance"));
    }

    @Override
    public void transfer(Transfer transfer) {
        onTransfer.reset();
        transfer.copyTo(onTransfer);

        if (!accountBalanceMap.containsKey(transfer.from())) {
            out.OnTransfer(onTransfer.success(false).reason("From account doesn't exist"));
            return;
        }
        if (!accountBalanceMap.containsKey(transfer.to())) {
            out.OnTransfer(onTransfer.success(false).reason("To account doesn't exist"));
            return;
        }
        if (transfer.amount() >= 0) {
            final Double fromBalance = accountBalanceMap.get(transfer.from());
            if (fromBalance >= transfer.amount()) {
                final Double toBalance = accountBalanceMap.get(transfer.to());
                accountBalanceMap.put(transfer.to(), toBalance + transfer.amount());
                accountBalanceMap.put(transfer.from(), fromBalance - transfer.amount());
                out.OnTransfer(onTransfer.success(true).reason(""));
                return;
            }
            out.OnTransfer(onTransfer.success(false).reason("Insufficient funds"));
            return;
        }
        out.OnTransfer(onTransfer.success(false).reason("Invalid amount"));
    }
}
