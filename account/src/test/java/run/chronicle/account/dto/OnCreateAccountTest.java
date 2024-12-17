package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        assertEquals("The sender field should match the 'sender' ShortText value.",
                "sender", ShortText.INSTANCE.asString(event.sender()));

        // Check target field
        assertEquals("The target field should match the 'target' ShortText value.",
                "target", ShortText.INSTANCE.asString(event.target()));

        // Check sendingTime field
        long expectedTime = net.openhft.chronicle.wire.converter.NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009");
        assertEquals("The sendingTime field should match the provided timestamp.",
                expectedTime, event.sendingTime());

        // Check the embedded CreateAccount object
        assertEquals("The embedded createAccount object should match the expected reference instance.",
                getCreateAccount(), event.createAccount());
    }

    /**
     * Tests that deserializing a {@link OnCreateAccount} event without the mandatory
     * createAccount field results in an {@link InvalidMarshallableException}. This
     * confirms that validation logic is working as intended.
     */
    @Test(expected = InvalidMarshallableException.class)
    public void missingCreateAccount() {
        String yaml = "" +
                "!run.chronicle.account.dto.OnCreateAccount {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001/02/03T04:05:06.007008009\n" +
                "}";

        OnCreateAccount event = Marshallable.fromString(yaml);

        // If we reach here, no exception was thrown, which means the test failed.
        Assert.fail("Expected InvalidMarshallableException due to missing createAccount field, but got: " + event);
    }
}
