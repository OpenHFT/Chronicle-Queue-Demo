package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static run.chronicle.account.dto.CreateAccountTest.getCreateAccount;

/**
 * Unit tests for {@link OnCreateAccount} event deserialization and validation.
 * <p>
 * These tests verify that a {@code OnCreateAccount} object can be correctly
 * deserialized from its YAML representation. Additionally, they confirm that
 * mandatory fields are present and that an {@link InvalidMarshallableException}
 * is thrown if any required fields (e.g., createAccount) are missing.
 */
public class OnCreateAccountTest {

    /**
     * Tests that a fully formed {@link OnCreateAccount} event is correctly
     * deserialized from a YAML string. Verifies that mandatory fields are set
     * and the embedded {@link CreateAccount} object matches expectations.
     */
    @Test
    public void testFromString() {
        String yaml = "" +
                "!run.chronicle.account.dto.OnCreateAccount {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001/02/03T04:05:06.007008009,\n" +
                "  createAccount: {\n" +
                "    sender: sender,\n" +
                "    target: target,\n" +
                "    sendingTime: 2001-02-03T04:05:06.007008009,\n" +
                "    name: name,\n" +
                "    account: 2,\n" +
                "    currency: CURR,\n" +
                "    balance: 1.0,\n" +
                "    overdraft: 3.0\n" +
                "  }\n" +
                "}";

        OnCreateAccount event = Marshallable.fromString(yaml);

        // Check sender field
        assertEquals("sender", ShortText.INSTANCE.asString(event.sender()), "sender");

        // Check target field
        assertEquals("target", ShortText.INSTANCE.asString(event.target()), "target");

        // Check sendingTime field
        long expectedTime = net.openhft.chronicle.wire.converter.NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009");
        assertEquals(expectedTime, event.sendingTime(), "sendingTime");

        // Check the embedded CreateAccount object
        assertEquals(getCreateAccount(), event.createAccount(), "createAccount");
    }

    /**
     * Tests that deserializing a {@link OnCreateAccount} event without the mandatory
     * createAccount field results in an {@link InvalidMarshallableException}. This
     * confirms that validation logic is working as intended.
     */
    @Test
    public void missingCreateAccount() {
        String yaml = "" +
                "!run.chronicle.account.dto.OnCreateAccount {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001/02/03T04:05:06.007008009\n" +
                "}";
        assertThrows(
                InvalidMarshallableException.class,
                () -> Marshallable.fromString(yaml),
                "missing createAccount should fail"
        );
    }
}
