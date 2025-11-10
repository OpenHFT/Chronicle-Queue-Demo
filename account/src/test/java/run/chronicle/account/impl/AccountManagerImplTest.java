//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account.impl;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.domain.AccountService;
import run.chronicle.account.dto.*;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountManagerImplTest {

    private static final long VAULT_ID = ShortText.INSTANCE.parse("vault");
    private static final long SENDER_ID = ShortText.INSTANCE.parse("gw1");
    private static final int EUR = (int) ShortText.INSTANCE.parse("EUR");
    private static final int USD = (int) ShortText.INSTANCE.parse("USD");

    private AccountManagerOut out;
    private AccountService accountService;
    private AccountManagerImpl manager;

    @BeforeEach
    void setUp() {
        out = mock(AccountManagerOut.class);
        accountService = mock(AccountService.class);
        manager = new AccountManagerImpl(out, accountService).id(VAULT_ID);
    }

    @Test
    void createAccount_Success() throws InvalidMarshallableException {
        CreateAccount ca = validCreateAccount("alice", 101013, EUR, 100, 10);

        when(accountService.tryCreateAccount(ca, VAULT_ID)).thenReturn(null);

        manager.createAccount(ca);

        // Verify onCreateAccount event was sent
        ArgumentCaptor<OnCreateAccount> captor = ArgumentCaptor.forClass(OnCreateAccount.class);
        verify(out).onCreateAccount(captor.capture());
        OnCreateAccount event = captor.getValue();
        assertThat(event.createAccount()).isEqualTo(ca);
        assertThat(event.sender()).isEqualTo(VAULT_ID);
        assertThat(event.target()).isEqualTo(SENDER_ID);
    }

    @Test
    void createAccount_Failure() throws InvalidMarshallableException {
        CreateAccount ca = validCreateAccount("bob", 101025, EUR, -1, 0);

        when(accountService.tryCreateAccount(ca, VAULT_ID)).thenReturn("invalid balance");

        manager.createAccount(ca);

        // Verify createAccountFailed event was sent
        ArgumentCaptor<CreateAccountFailed> captor = ArgumentCaptor.forClass(CreateAccountFailed.class);
        verify(out).createAccountFailed(captor.capture());
        CreateAccountFailed event = captor.getValue();
        assertThat(event.createAccount()).isEqualTo(ca);
        assertThat(event.reason()).isEqualTo("invalid balance");
        assertThat(event.sender()).isEqualTo(VAULT_ID);
        assertThat(event.target()).isEqualTo(SENDER_ID);
    }

    @Test
    void createAccount_InvalidDtoThrows() throws InvalidMarshallableException {
        CreateAccount invalidDto = new CreateAccount()
                .sender(0) // invalid sender
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:00:00"))
                .name("invalid")
                .account(0) // invalid account
                .currency(EUR)
                .balance(10);

        // Simulate that AccountService will throw InvalidMarshallableException
        doThrow(new InvalidMarshallableException("sender must be set"))
                .when(accountService).tryCreateAccount(invalidDto, VAULT_ID);

        try {
            manager.createAccount(invalidDto);
        } catch (InvalidMarshallableException e) {
            // expected
        }

        // Since an exception was thrown, no events should be emitted
        verifyNoInteractions(out);
    }

    @Test
    void transfer_Success() throws InvalidMarshallableException {
        Transfer transfer = validTransfer(101013, 101025, EUR, 50, "Payment");

        when(accountService.tryTransfer(transfer, VAULT_ID)).thenReturn(null);

        manager.transfer(transfer);

        // Verify onTransfer event was sent
        ArgumentCaptor<OnTransfer> captor = ArgumentCaptor.forClass(OnTransfer.class);
        verify(out).onTransfer(captor.capture());
        OnTransfer event = captor.getValue();
        assertThat(event.transfer()).isEqualTo(transfer);
        assertThat(event.sender()).isEqualTo(VAULT_ID);
        assertThat(event.target()).isEqualTo(transfer.sender());
    }

    @Test
    void transfer_Failure() throws InvalidMarshallableException {
        Transfer transfer = validTransfer(101013, 101025, USD, 1000, "HugePayment");

        when(accountService.tryTransfer(transfer, VAULT_ID)).thenReturn("insufficient funds");

        manager.transfer(transfer);

        // Verify transferFailed event was sent
        ArgumentCaptor<TransferFailed> captor = ArgumentCaptor.forClass(TransferFailed.class);
        verify(out).transferFailed(captor.capture());
        TransferFailed event = captor.getValue();
        assertThat(event.reason()).isEqualTo("insufficient funds");
        assertThat(event.transfer()).isEqualTo(transfer);
        assertThat(event.sender()).isEqualTo(VAULT_ID);
        assertThat(event.target()).isEqualTo(transfer.sender());
    }

    @Test
    void transfer_InvalidDtoThrows() throws InvalidMarshallableException {
        Transfer invalidTransfer = new Transfer()
                .sender(0) // invalid sender
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T10:01:00"))
                .from(0) // invalid from
                .to(101025)
                .currency(EUR)
                .amount(10)
                .reference(net.openhft.chronicle.bytes.Bytes.from("Test"));

        doThrow(new InvalidMarshallableException("sender must be set"))
                .when(accountService).tryTransfer(invalidTransfer, VAULT_ID);

        manager.transfer(invalidTransfer);

        // Verify transferFailed event due to exception in service
        ArgumentCaptor<TransferFailed> captor = ArgumentCaptor.forClass(TransferFailed.class);
        verify(out).transferFailed(captor.capture());
        TransferFailed event = captor.getValue();
        assertThat(event.reason()).contains("sender must be set");
        assertThat(event.sender()).isEqualTo(VAULT_ID);
        assertThat(event.target()).isEqualTo(invalidTransfer.sender()); // which is 0
    }

    @Test
    void checkPoint_SameTarget() {
        CheckPoint cp = new CheckPoint()
                .sender(ShortText.INSTANCE.parse("gw2"))
                .target(VAULT_ID)
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T11:00:00"));

        Map<Long, CreateAccount> accounts = new LinkedHashMap<>();
        CreateAccount ca = validCreateAccount("alice", 101013, EUR, 50, 0);
        accounts.put(101013L, ca);

        when(accountService.getAllAccounts()).thenReturn(accounts);

        manager.checkPoint(cp);

        // Verify startCheckpoint, onCreateAccount for each account, and endCheckpoint
        verify(out).startCheckpoint(cp);
        ArgumentCaptor<OnCreateAccount> captor = ArgumentCaptor.forClass(OnCreateAccount.class);
        verify(out).onCreateAccount(captor.capture());
        OnCreateAccount event = captor.getValue();
        assertThat(event.createAccount()).isEqualTo(ca);
        verify(out).endCheckpoint(cp);
    }

    @Test
    void checkPoint_DifferentTargetIgnored() {
        CheckPoint cp = new CheckPoint()
                .sender(ShortText.INSTANCE.parse("gw2"))
                .target(ShortText.INSTANCE.parse("other"))
                .sendingTime(NanoTime.INSTANCE.parse("2023-01-20T11:00:00"));

        manager.checkPoint(cp);

        // Different target means no interaction
        verifyNoInteractions(out);
        verifyNoInteractions(accountService);
    }

    // Helper methods
    private CreateAccount validCreateAccount(String name, long accountNum, int currency, double balance, double overdraft) {
        return new CreateAccount()
                .sender(SENDER_ID)
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
                .reference(net.openhft.chronicle.bytes.Bytes.from(reference));
    }
}
