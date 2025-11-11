/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.domain;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import run.chronicle.account.dto.CreateAccount;
import run.chronicle.account.dto.Transfer;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;

import static net.openhft.chronicle.bytes.Bytes.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountServiceTest {
    private static final long VAULT_ID = ShortText.INSTANCE.parse("vault");
    private static final int EUR = (int) ShortText.INSTANCE.parse("EUR");
    private static final int USD = (int) ShortText.INSTANCE.parse("USD");

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService();
    }

    @Test
    void shouldCreateAccountSuccessfully() throws InvalidMarshallableException {
        CreateAccount ca = validCreateAccount("alice", 101013, EUR, 100, 10);
        String failureReason = accountService.tryCreateAccount(ca, VAULT_ID);

        assertThat(failureReason).isNull();
        assertThat(accountService.getAllAccounts()).containsKey(101013L);
    }

    @Test
    void shouldFailToCreateAccountIfTargetMismatch() throws InvalidMarshallableException {
        CreateAccount ca = validCreateAccount("alice", 101013, EUR, 100, 10);
        String failureReason = accountService.tryCreateAccount(ca, ShortText.INSTANCE.parse("other"));

        assertThat(failureReason).isEqualTo("target mismatch");
        assertThat(accountService.getAllAccounts()).doesNotContainKey(101013L);
    }

    @Test
    void shouldFailToCreateAccountIfAlreadyExists() throws InvalidMarshallableException {
        CreateAccount ca1 = validCreateAccount("charlie", 101040, EUR, 50, 0);
        CreateAccount ca2 = validCreateAccount("charlie2", 101040, EUR, 60, 0);

        accountService.tryCreateAccount(ca1, VAULT_ID);
        String failureReason = accountService.tryCreateAccount(ca2, VAULT_ID);

        assertThat(failureReason).isEqualTo("account already exists");
        assertThat(accountService.getAllAccounts()).hasSize(1);
    }

    @Test
    void shouldTransferFundsSuccessfully() throws InvalidMarshallableException {
        CreateAccount from = validCreateAccount("alice", 101013, EUR, 100, 10);
        CreateAccount to = validCreateAccount("bob", 101025, EUR, 50, 0);

        accountService.tryCreateAccount(from, VAULT_ID);
        accountService.tryCreateAccount(to, VAULT_ID);

        Transfer transfer = validTransfer(101013, 101025, EUR, 30, "Payment");
        String failureReason = accountService.tryTransfer(transfer, VAULT_ID);

        assertThat(failureReason).isNull();
        assertThat(accountService.getAllAccounts().get(101013L).balance()).isEqualTo(70.0);
        assertThat(accountService.getAllAccounts().get(101025L).balance()).isEqualTo(80.0);
    }

    @Test
    void shouldFailTransferIfTargetMismatch() throws InvalidMarshallableException {
        CreateAccount from = validCreateAccount("alice", 101013, EUR, 100, 10);
        CreateAccount to = validCreateAccount("bob", 101025, EUR, 50, 0);

        accountService.tryCreateAccount(from, VAULT_ID);
        accountService.tryCreateAccount(to, VAULT_ID);

        Transfer transfer = validTransfer(101013, 101025, EUR, 10, "Test");
        String failureReason = accountService.tryTransfer(transfer, ShortText.INSTANCE.parse("other"));

        assertThat(failureReason).isEqualTo("target mismatch");
    }

    @Test
    void shouldFailTransferIfFromAccountDoesNotExist() throws InvalidMarshallableException {
        CreateAccount to = validCreateAccount("bob", 101025, EUR, 50, 0);
        accountService.tryCreateAccount(to, VAULT_ID);

        Transfer transfer = validTransfer(999999, 101025, EUR, 10, "Test");
        String failureReason = accountService.tryTransfer(transfer, VAULT_ID);

        assertThat(failureReason).isEqualTo("from account doesn't exist");
    }

    @Test
    void shouldFailTransferIfToAccountDoesNotExist() throws InvalidMarshallableException {
        CreateAccount from = validCreateAccount("alice", 101013, EUR, 100, 10);
        accountService.tryCreateAccount(from, VAULT_ID);

        Transfer transfer = validTransfer(101013, 999999, EUR, 10, "Test");
        String failureReason = accountService.tryTransfer(transfer, VAULT_ID);

        assertThat(failureReason).isEqualTo("to account doesn't exist");
    }

    @Test
    void shouldFailTransferIfCurrencyMismatch() throws InvalidMarshallableException {
        CreateAccount from = validCreateAccount("alice", 101013, EUR, 100, 10);
        CreateAccount to = validCreateAccount("bob", 101025, USD, 50, 0);

        accountService.tryCreateAccount(from, VAULT_ID);
        accountService.tryCreateAccount(to, VAULT_ID);

        Transfer transfer = validTransfer(101013, 101025, EUR, 10, "Mismatched Currency");
        String failureReason = accountService.tryTransfer(transfer, VAULT_ID);

        assertThat(failureReason).isEqualTo("to account currency doesn't match");
    }

    @Test
    void shouldFailTransferIfInsufficientFunds() throws InvalidMarshallableException {
        CreateAccount from = validCreateAccount("alice", 101013, EUR, 10, 0);
        CreateAccount to = validCreateAccount("charlie", 101040, EUR, 50, 0);

        accountService.tryCreateAccount(from, VAULT_ID);
        accountService.tryCreateAccount(to, VAULT_ID);

        Transfer transfer = validTransfer(101013, 101040, EUR, 100, "Too Big");
        String failureReason = accountService.tryTransfer(transfer, VAULT_ID);

        assertThat(failureReason).isEqualTo("insufficient funds");
    }

    @Test
    void shouldThrowExceptionForInvalidCreateAccountDto() {
        CreateAccount invalidDto = new CreateAccount()
                .sender(0) // invalid sender
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:00:00"))
                .name("invalid")
                .account(0) // invalid account
                .currency(EUR)
                .balance(10);

        assertThatThrownBy(() -> accountService.tryCreateAccount(invalidDto, VAULT_ID))
                .isInstanceOf(InvalidMarshallableException.class);
    }

    @Test
    void shouldThrowExceptionForInvalidTransferDto() {
        Transfer invalidTransfer = new Transfer()
                .sender(0) // invalid sender
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:00:00"))
                .from(0) // invalid from
                .to(101025)
                .currency(EUR)
                .amount(10)
                .reference(from("Test"));

        assertThatThrownBy(() -> accountService.tryTransfer(invalidTransfer, VAULT_ID))
                .isInstanceOf(InvalidMarshallableException.class);
    }

    // Helper methods to create valid DTOs
    private CreateAccount validCreateAccount(String name, long accountNum, int currency, double balance, double overdraft) {
        return new CreateAccount()
                .sender(ShortText.INSTANCE.parse("gw1"))
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:00:00"))
                .name(name)
                .account(accountNum)
                .currency(currency)
                .balance(balance)
                .overdraft(overdraft);
    }

    private Transfer validTransfer(long from, long to, int currency, double amount, String reference) {
        return new Transfer()
                .sender(ShortText.INSTANCE.parse("gw2"))
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:01:00"))
                .from(from)
                .to(to)
                .currency(currency)
                .amount(amount)
                .reference(from(reference));
    }
}
