package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransferFailedTest {

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

    @Test
    public void testToString() {
        assertEquals(EXPECTED,
                new TransferFailed()
                        .target(ShortText.INSTANCE.parse("sender"))
                        .sender(ShortText.INSTANCE.parse("target"))
                        .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.777888999"))
                        .reason("reasons")
                        .transfer(TransferTest.getTransfer())
                        .toString());
    }

    @Test
    public void testFromString() {
        TransferFailed tf = Marshallable.fromString(EXPECTED);
        assertEquals(TransferTest.getTransfer(), tf.transfer());
        assertEquals("reasons", tf.reason());
    }

    /**
     * This test verifies the scenario where a required field in TransferFailed object is missing.
     * It is expected that when the {@link Marshallable#fromString(CharSequence)} method is used to parse a String
     * that represents a TransferFailed object missing a required field, an InvalidMarshallableException will be thrown.
     *
     * @throws InvalidMarshallableException if a required field in the marshalled String is missing.
     */
    @Test(expected = InvalidMarshallableException.class)
    public void missingFieldInTransferFailed() {
        OnTransfer tok = Marshallable.fromString("" +
                "!run.chronicle.account.dto.TransferFailed {\n" +
                "  sender: target,\n" +
                "  target: sender,\n" +
                "  sendingTime: 2001-02-03T04:05:06.777888999," +
                "  reason: None\n" +
                "}\n");

        fail(tok.toString());
    }

    /**
     * Tests that an exception is thrown when the `reason` field is missing from a `TransferFailed` DTO.
     *
     * @throws InvalidMarshallableException if the `TransferFailed` DTO is not valid
     */
    @Test(expected = InvalidMarshallableException.class)
    public void missingReasonFieldInTransferFailed() {
        // Create a `TransferFailed` DTO without the `reason` field.
        OnTransfer tok = Marshallable.fromString("" +
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
                "}\n");

        // Fail if the `TransferFailed` DTO is valid.
        fail(tok.toString());
    }
}
