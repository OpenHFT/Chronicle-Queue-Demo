package run.chronicle.account.dto;

import net.openhft.chronicle.wire.Marshallable;
import net.openhft.chronicle.wire.converter.Base85;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static run.chronicle.account.dto.CreateAccountTest.getCreateAccount;

public class OnCreateAccountTest {
    @Test
    public void testFromString() {
        OnCreateAccount asf = Marshallable.fromString("" +
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
                "    balance: 1.0" +
                "  }\n" +
                "}");
        assertEquals("sender", Base85.INSTANCE.asString(asf.sender()));
        assertEquals("target", Base85.INSTANCE.asString(asf.target()));
        assertEquals(getCreateAccount(), asf.createAccount());
    }

}