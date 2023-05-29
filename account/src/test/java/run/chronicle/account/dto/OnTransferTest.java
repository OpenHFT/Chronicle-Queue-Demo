package run.chronicle.account.dto;

import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.Test;

import static org.junit.Assert.*;
import static run.chronicle.account.dto.TransferTest.getTransfer;

public class OnTransferTest {

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

    @Test
    public void testToString() {
        assertEquals(EXPECTED,
                new OnTransfer()
                        .target(Base85.INSTANCE.parse("sender"))
                        .sender(Base85.INSTANCE.parse("target"))
                        .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.777888999"))
                        .transfer(getTransfer())
                        .toString());
    }

    @Test
    public void testFromString() {
        OnTransfer tok = Marshallable.fromString(EXPECTED);
        assertFalse(tok.usesSelfDescribingMessage());
        assertEquals(TransferTest.getTransfer(), tok.transfer());
    }

    @Test(expected = InvalidMarshallableException.class)
    public void missingTransfer() {
        OnTransfer tok = Marshallable.fromString("" +
                "!run.chronicle.account.dto.OnTransfer {\n" +
                "  sender: target,\n" +
                "  target: sender,\n" +
                "  sendingTime: 2001-02-03T04:05:06.777888999,\n" +
                "}\n");
        fail(tok.toString());
    }
}
