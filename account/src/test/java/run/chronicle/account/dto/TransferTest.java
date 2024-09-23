package run.chronicle.account.dto;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * This class TransferTest is a test class that tests the Transfer class.
 */
public class TransferTest {
    /**
     * This method creates a Transfer object with the following values:
     * @return a Transfer object
     */
    static Transfer getTransfer() {
        return new Transfer()
                .sender(ShortText.INSTANCE.parse("sender"))
                .target(ShortText.INSTANCE.parse("target"))
                .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009"))
                .amount(1)
                .currency((int) ShortText.INSTANCE.parse("CURR"))
                .from(12345)
                .to(67890)
                .reference(Bytes.from("reference"));
    }

    /**
     * This test checks that the toString method of the Transfer class
     */
    @Test
    public void testToString() {
        Transfer transfer = getTransfer();
        assertEquals("" +
                        "!run.chronicle.account.dto.Transfer {\n" +
                        "  sender: sender,\n" +
                        "  target: target,\n" +
                        "  sendingTime: 2001-02-03T04:05:06.007008009,\n" +
                        "  from: 12345,\n" +
                        "  to: 67890,\n" +
                        "  currency: CURR,\n" +
                        "  amount: 1.0,\n" +
                        "  reference: reference\n" +
                        "}\n",
                transfer.toString());
        assertFalse(transfer.usesSelfDescribingMessage());

    }
}
