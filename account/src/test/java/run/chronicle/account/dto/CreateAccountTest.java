package run.chronicle.account.dto;

import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link CreateAccount} class.
 * <p>
 * These tests verify that a {@code CreateAccount} object is constructed correctly,
 * serialized as expected via {@code toString()}, and that its fields are properly set.
 * The test uses known, fixed values for fields such as sender, target, and sendingTime
 * to produce deterministic outputs.
 */
public class CreateAccountTest {

    /**
     * Helper method to create a fully-initialised {@link CreateAccount} object
     * for testing. Uses consistent, known values for all fields.
     *
     * @return a {@code CreateAccount} instance populated with test data
     */
    static CreateAccount getCreateAccount() {
        return new CreateAccount()
                .sender(ShortText.INSTANCE.parse("sender"))
                .target(ShortText.INSTANCE.parse("target"))
                .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009"))
                .balance(1)
                .account(2)
                .overdraft(3)
                .currency((int) ShortText.INSTANCE.parse("CURR"))
                .name("name");
    }

    /**
     * Tests the {@code toString()} representation of a {@link CreateAccount} object.
     * Also verifies selected fields (e.g., sendingTime, name) to ensure they are set correctly.
     */
    @Test
    public void testToString() {
        CreateAccount createAccount = getCreateAccount();

        // Check the string representation line-by-line to ensure it matches the expected format.
        String expected = "" +
                "!run.chronicle.account.dto.CreateAccount {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001-02-03T04:05:06.007008009,\n" +
                "  name: name,\n" +
                "  account: 2,\n" +
                "  currency: CURR,\n" +
                "  balance: 1.0,\n" +
                "  overdraft: 3.0\n" +
                "}\n";
        assertEquals(
                expected,
                createAccount.toString(),
                "The toString() output of CreateAccount should match the expected YAML-like format."
        );

        // Verify the parsed sendingTime matches what was set.
        long expectedTime = NanoTime.INSTANCE.parse("2001-02-03T04:05:06.007008009");
        assertEquals(
                expectedTime,
                createAccount.sendingTime(),
                "The sendingTime field should match the expected nanosecond timestamp."
        );

        // Verify the name field is correctly set.
        assertEquals("name", createAccount.name(), "The name field should match the initialised value.");

        // Additional checks to ensure all fields are as expected.
        assertEquals(
                "sender",
                ShortText.INSTANCE.asString(createAccount.sender()),
                "The sender field should match the initialised value."
        );
        assertEquals(
                "target",
                ShortText.INSTANCE.asString(createAccount.target()),
                "The target field should match the initialised value."
        );
        assertEquals(2L, createAccount.account(), "The account number should match the initialised value.");
        assertEquals(
                "CURR",
                ShortText.INSTANCE.asString(createAccount.currency()),
                "The currency field should match the initialised value."
        );
        assertEquals(1.0, createAccount.balance(), 0.0, "The balance should match the initialised value.");
        assertEquals(3.0, createAccount.overdraft(), 0.0, "The overdraft should match the initialised value.");
    }
}
