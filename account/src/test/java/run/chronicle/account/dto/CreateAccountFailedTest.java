package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static run.chronicle.account.dto.CreateAccountTest.getCreateAccount;

/**
 * Unit tests for {@link CreateAccountFailed} events.
 * <p>
 * These tests verify that a {@code CreateAccountFailed} object can be deserialized
 * correctly from a YAML representation and that appropriate validation errors
 * are thrown when required fields are missing.
 * <p>
 * Validations:
 * - Ensure all mandatory fields (sender, target, sendingTime, createAccount, reason) are present.
 * - Confirm that absence of createAccount or reason fields results in an InvalidMarshallableException.
 */
public class CreateAccountFailedTest {

    /**
     * Tests that a fully populated {@link CreateAccountFailed} event can be deserialized
     * from a YAML string. Verifies that all fields match expected values.
     */
    @Test
    public void testFromString() {
        // YAML representation of a valid CreateAccountFailed event.
        String yaml = "" +
                "!run.chronicle.account.dto.CreateAccountFailed {\n" +
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
                "  },\n" +
                "  reason: reasons\n" +
                "}";

        CreateAccountFailed event = Marshallable.fromString(yaml);

        // Verify fields are set as expected
        assertEquals("Sender should match the provided ShortText 'sender'.",
                "sender", ShortText.INSTANCE.asString(event.sender()));
        assertEquals("Target should match the provided ShortText 'target'.",
                "target", ShortText.INSTANCE.asString(event.target()));
        assertEquals("Reason should match the provided reason string.",
                "reasons", event.reason());
        assertEquals("The embedded createAccount object should match the expected CreateAccount.",
                getCreateAccount(), event.createAccount());
    }

    /**
     * Tests that attempting to deserialize a CreateAccountFailed event with no createAccount field
     * results in an InvalidMarshallableException.
     *
     * The createAccount field is mandatory, so if it's missing, the validation should fail.
     */
    @Test(expected = InvalidMarshallableException.class)
    public void missingCreateAccount() {
        String yaml = "" +
                "!run.chronicle.account.dto.CreateAccountFailed {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001/02/03T04:05:06.007008009,\n" +
                "}";
        CreateAccountFailed event = Marshallable.fromString(yaml);

        // If we reach this line, the test failed to throw the expected exception.
        fail("Expected InvalidMarshallableException due to missing createAccount field. Event: " + event);
    }

    /**
     * Tests that attempting to deserialize a CreateAccountFailed event with no reason field
     * results in an InvalidMarshallableException.
     *
     * The reason field is mandatory, so if it's missing, the validation should fail.
     */
    @Test(expected = InvalidMarshallableException.class)
    public void missingReason() {
        // YAML missing 'reason' field
        String yaml = "" +
                "!run.chronicle.account.dto.CreateAccountFailed {\n" +
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
                "    balance: 1.0\n" +
                "  }\n" +
                "}";

        CreateAccountFailed event = Marshallable.fromString(yaml);

        // If we reach this line, the test failed to throw the expected exception.
        fail("Expected InvalidMarshallableException due to missing reason field. Event: " + event);
    }
}
