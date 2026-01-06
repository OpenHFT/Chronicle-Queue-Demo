package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link TransferFailed} DTO.
 * <p>
 * These tests verify:
 * <ul>
 *   <li>Serialization: Ensuring {@link TransferFailed#toString()} produces the expected YAML representation.</li>
 *   <li>Deserialization: Confirming that a {@link TransferFailed} object can be reconstructed from YAML.</li>
 *   <li>Validation: Ensuring that missing required fields result in {@link InvalidMarshallableException}.</li>
 * </ul>
 */
public class TransferFailedTest {

    /**
     * A fully populated {@link TransferFailed} YAML representation used in these tests.
     */
    public static final String EXPECTED = "" +
            "!run.chronicle.account.dto.TransferFailed {\n" +
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
            "  },\n" +
            "  reason: reasons\n" +
            "}\n";

    /**
     * Tests that calling {@link TransferFailed#toString()} on a fully populated object
     * matches the expected YAML string.
     */
    @Test
    public void testToString() {
        TransferFailed tf = new TransferFailed()
                .target(ShortText.INSTANCE.parse("sender"))
                .sender(ShortText.INSTANCE.parse("target"))
                .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.777888999"))
                .reason("reasons")
                .transfer(TransferTest.getTransfer());

        assertEquals(EXPECTED, tf.toString(), "TransferFailed.toString YAML");
    }

    /**
     * Tests that a {@link TransferFailed} object can be deserialized from the EXPECTED YAML,
     * and that all fields match the original expected values.
     */
    @Test
    public void testFromString() {
        TransferFailed tf = Marshallable.fromString(EXPECTED);

        // Verify that the transfer object matches the expected instance.
        assertEquals(TransferTest.getTransfer(), tf.transfer(), "transfer");

        // Verify that the reason field matches the expected value.
        assertEquals("reasons", tf.reason(), "reason");
    }

    /**
     * Tests that deserializing a {@link TransferFailed} event without the required 'transfer' field
     * throws an {@link InvalidMarshallableException}.
     * <p>
     * Here, the 'transfer' field is missing. According to the DTO's requirements,
     * this should cause validation to fail.
     */
    @Test
    public void missingFieldInTransferFailed() {
        String yamlMissingTransfer = "" +
                "!run.chronicle.account.dto.TransferFailed {\n" +
                "  sender: target,\n" +
                "  target: sender,\n" +
                "  sendingTime: 2001-02-03T04:05:06.777888999,\n" +
                "  reason: None\n" +
                "}\n";
        assertThrows(
                InvalidMarshallableException.class,
                () -> Marshallable.fromString(yamlMissingTransfer),
                "missing transfer should fail"
        );
    }

    /**
     * Tests that deserializing a {@link TransferFailed} event without the required 'reason' field
     * throws an {@link InvalidMarshallableException}.
     * <p>
     * Here, the 'reason' field is missing. This should cause validation to fail.
     */
    @Test
    public void missingReasonFieldInTransferFailed() {
        String yamlMissingReason = "" +
                "!run.chronicle.account.dto.TransferFailed {\n" +
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
        assertThrows(
                InvalidMarshallableException.class,
                () -> Marshallable.fromString(yamlMissingReason),
                "missing reason should fail"
        );
    }
}
