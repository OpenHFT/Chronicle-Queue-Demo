package run.chronicle.account.util;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.onoes.ExceptionKey;
import org.junit.After;
import org.junit.Test;
import run.chronicle.account.dto.*;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class LogsAccountManagerOutTest {
    @After
    public void reset() {
        Jvm.resetExceptionHandlers();
    }
    @Test
    public void expectOutput() {
        Map<ExceptionKey, Integer> recorded = Jvm.recordExceptions(true);
        LogsAccountManagerOut out = new LogsAccountManagerOut();
        out.createAccountFailed(new CreateAccountFailed());
        out.endCheckpoint(new CheckPoint());
        out.jvmError("jvm error");
        out.onCreateAccount(new OnCreateAccount());
        out.onTransfer(new OnTransfer());
        out.startCheckpoint(new CheckPoint());
        out.transferFailed(new TransferFailed());
        String collect = recorded.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        assertEquals("" +
                "ExceptionKey{level=WARN, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='createAccountFailed !run.chronicle.account.dto.CreateAccountFailed {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0,\n" +
                "  createAccount: !!null \"\",\n" +
                "  reason: !!null \"\"\n" +
                "}\n" +
                "', throwable=}\n" +
                "ExceptionKey{level=DEBUG, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='endCheckpoint !run.chronicle.account.dto.CheckPoint {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0\n" +
                "}\n" +
                "', throwable=}\n" +
                "ExceptionKey{level=ERROR, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='jvmError jvm error', throwable=}\n" +
                "ExceptionKey{level=DEBUG, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='onCreateAccount !run.chronicle.account.dto.OnCreateAccount {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0,\n" +
                "  createAccount: !!null \"\"\n" +
                "}\n" +
                "', throwable=}\n" +
                "ExceptionKey{level=DEBUG, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='onTransfer !run.chronicle.account.dto.OnTransfer {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0,\n" +
                "  transfer: !!null \"\"\n" +
                "}\n" +
                "', throwable=}\n" +
                "ExceptionKey{level=DEBUG, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='startCheckpoint !run.chronicle.account.dto.CheckPoint {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0\n" +
                "}\n" +
                "', throwable=}\n" +
                "ExceptionKey{level=WARN, clazz=class run.chronicle.account.util.LogsAccountManagerOut, message='transferFailed !run.chronicle.account.dto.TransferFailed {\n" +
                "  sender: \"\",\n" +
                "  target: \"\",\n" +
                "  sendingTime: 0,\n" +
                "  transfer: !!null \"\",\n" +
                "  reason: !!null \"\"\n" +
                "}\n" +
                "', throwable=}", collect);

    }

}