package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static run.chronicle.account.dto.TransferTest.getTransfer;

/**
 * Unit tests for the {@link OnTransfer} DTO.
 * <p>
 * These tests verify that:
 * <ul>
 *   <li>{@code OnTransfer} objects can be correctly serialized to and deserialized from their YAML representation.</li>
 *   <li>Mandatory fields (like the embedded {@link Transfer}) are present and validated.</li>
 *   <li>The {@link OnTransfer#usesSelfDescribingMessage()} method behaves as expected.</li>
 * </ul>
 */
public class OnTransferTest {

    /**
     * A YAML representation of a fully populated {@link OnTransfer} instance,
     * including a nested {@link Transfer} object and all required fields.
     */
    public static final String EXPECTED = "" +
            "!run.chronicle.account.dto.OnTransfer {\n" +
            "  sender: target,\n" +
            "  target: sender,\n" +
            "  sendingTime: 2001-02-03T04:05:06.777888999,\n" +
            "  transfer: {\n" +
            "    sender: sender,\n" +
            "    target: target,\n" +
            "    sendingTime: 2001-02-03T04:05:06.007008009,\n" +
            "    from: 12345,\n" +
            "    to: 67890,\n" +
            "    currency: CURR,\n" +
            "    amount: 1.0,\n" +
            "    reference: reference\n" +
            "  }\n" +
            "}\n";

    /**
     * Tests that the {@link OnTransfer#toString()} method produces the expected YAML representation.
     * Also serves as a regression check to ensure the serialized format remains stable.
     */
    @Test
    public void testToString() {
        OnTransfer event = new OnTransfer()
                .target(ShortText.INSTANCE.parse("sender"))
                .sender(ShortText.INSTANCE.parse("target"))
                .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.777888999"))
                .transfer(getTransfer());

        assertEquals(EXPECTED, event.toString(), "OnTransfer.toString YAML");
    }

    /**
     * Tests that an {@link OnTransfer} object can be deserialized from a YAML string,
     * and that the resulting object matches the expected state.
     */
    @Test
    public void testFromString() {
        OnTransfer event = Marshallable.fromString(EXPECTED);

        // Verify that the event does not use a self-describing message format.
        assertFalse(
                event.usesSelfDescribingMessage(),
                "usesSelfDescribingMessage() should return false for OnTransfer events."
        );

        // Verify that the transfer object matches the expected reference Transfer.
        assertEquals(getTransfer(), event.transfer(), "transfer");

        // Check sender and target fields
        String senderStr = ShortText.INSTANCE.asString(event.sender());
        String targetStr = ShortText.INSTANCE.asString(event.target());
        assertEquals("target", senderStr, "sender");
        assertEquals("sender", targetStr, "target");

        // Check sendingTime field
        long expectedTime = NanoTime.INSTANCE.parse("2001-02-03T04:05:06.777888999");
        assertEquals(expectedTime, event.sendingTime(), "sendingTime");
    }

    /**
     * Tests that attempting to deserialize an {@link OnTransfer} event without the mandatory
     * transfer field results in an {@link InvalidMarshallableException}. This ensures that
     * validation logic is properly enforced.
     */
    @Test
    public void missingTransfer() {
        String yamlWithoutTransfer = "" +
                "!run.chronicle.account.dto.OnTransfer {\n" +
                "  sender: target,\n" +
                "  target: sender,\n" +
                "  sendingTime: 2001-02-03T04:05:06.777888999,\n" +
                "}\n";
        assertThrows(
                InvalidMarshallableException.class,
                () -> Marshallable.fromString(yamlWithoutTransfer),
                "missing transfer should fail"
        );
    }
}
