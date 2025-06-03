package run.chronicle.account.util;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.onoes.ExceptionKey;
import org.junit.After;
import org.junit.Test;
import run.chronicle.account.dto.*;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link LogsAccountManagerOut}, verifying that all events logged by this mock implementation
 * produce the expected logging output. This ensures that logging behavior remains stable and predictable.
 */
public class LogsAccountManagerOutTest {

    /**
     * Resets the Jvm exception handlers after each test to prevent side effects between tests.
     */
    @After
    public void reset() {
        Jvm.resetExceptionHandlers();
    }

    /**
     * Tests that the {@link LogsAccountManagerOut} correctly logs various events to Jvm's exception handlers.
     * This includes verifying that all output events are captured with the expected log level, message, and format.
     */
    @Test
    public void expectOutput() {
        // Start recording exceptions thrown/logged by Jvm for verification.
        Map<ExceptionKey, Integer> recorded = Jvm.recordExceptions(true);

        // Instantiate a LogsAccountManagerOut and emit various events.
        LogsAccountManagerOut out = new LogsAccountManagerOut();
        out.createAccountFailed(new CreateAccountFailed()); // Should produce a WARN log
        out.endCheckpoint(new CheckPoint());                // Should produce a DEBUG log
        out.jvmError("jvm error");                          // Should produce an ERROR log
        out.onCreateAccount(new OnCreateAccount());         // Should produce a DEBUG log
        out.onTransfer(new OnTransfer());                   // Should produce a DEBUG log
        out.startCheckpoint(new CheckPoint());              // Should produce a DEBUG log
        out.transferFailed(new TransferFailed());           // Should produce a WARN log

        // Filter and collect logs only from the same package as this test.
        String collectedLog = recorded.keySet().stream()
                .filter(e -> e.clazz().getPackage().equals(LogsAccountManagerOutTest.class.getPackage()))
                .map(Object::toString)
                .collect(Collectors.joining("\n"));

        // Expected log output. Each event should match these lines exactly.
        String expected = "" +
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
                "', throwable=}";

        // Assert that the collected log matches the expected output exactly.
        assertEquals("The recorded exception logs should match the expected logging output.",
                expected, collectedLog);
    }
}
