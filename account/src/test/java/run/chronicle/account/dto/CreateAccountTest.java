package run.chronicle.account.dto;

import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CreateAccountTest {
    static CreateAccount getCreateAccount() {
        return new CreateAccount()
                .sender(Base85.INSTANCE.parse("sender"))
                .target(Base85.INSTANCE.parse("target"))
                .sendingTime(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009"))
                .balance(1)
                .account(2)
                .currency((int) Base85.INSTANCE.parse("CURR"))
                .name("name");
    }

    @Test
    public void testToString() {
        CreateAccount createAccount = getCreateAccount();
        assertEquals("" +
                        "!run.chronicle.account.dto.CreateAccount {\n" +
                        "  sender: sender,\n" +
                        "  target: target,\n" +
                        "  sendingTime: 2001-02-03T04:05:06.007008009,\n" +
                        "  name: name,\n" +
                        "  account: 2,\n" +
                        "  currency: CURR,\n" +
                        "  balance: 1.0\n" +
                        "}\n",
                createAccount.toString());
        assertEquals(NanoTime.INSTANCE.parse("2001-02-03T04:05:06.007008009"),
                createAccount.sendingTime());
        assertEquals("name", createAccount.name());
    }
}
