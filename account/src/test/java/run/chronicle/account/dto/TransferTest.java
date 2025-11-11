/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.account.dto;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.converter.ShortText;
import net.openhft.chronicle.wire.converter.NanoTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link Transfer} DTO.
 * <p>
 * These tests ensure that:
 * <ul>
 *   <li>A {@link Transfer} object is correctly populated and all fields match expected values.</li>
 *   <li>The {@code toString()} method produces a stable, expected YAML-like representation.</li>
 *   <li>The {@link Transfer#usesSelfDescribingMessage()} method returns the correct value.</li>
 * </ul>
 */
public class TransferTest {

    /**
     * Creates a {@link Transfer} instance with predetermined, known values for all fields.
     * This method acts as a reference object for testing and comparison.
     *
     * @return a fully populated {@link Transfer} instance for testing
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
     * Tests that the {@code toString()} method of {@link Transfer} produces the expected output,
     * and verifies the {@link Transfer#usesSelfDescribingMessage()} property.
     * <p>
     * This ensures the serialization format remains consistent and that the Transfer DTO
     * does not mistakenly switch to a self-describing message format.
     */
    @Test
    public void testToString() {
        Transfer transfer = getTransfer();

        String expectedToString = "" +
                "!run.chronicle.account.dto.Transfer {\n" +
                "  sender: sender,\n" +
                "  target: target,\n" +
                "  sendingTime: 2001-02-03T04:05:06.007008009,\n" +
                "  from: 12345,\n" +
                "  to: 67890,\n" +
                "  currency: CURR,\n" +
                "  amount: 1.0,\n" +
                "  reference: reference\n" +
                "}\n";

        // Check that the string representation matches the expected format.
        assertEquals("The toString() output should match the expected YAML representation.",
                expectedToString, transfer.toString());

        // Verify that usesSelfDescribingMessage() returns false as expected.
        assertFalse("usesSelfDescribingMessage() should return false for Transfer events.",
                transfer.usesSelfDescribingMessage());

        // Additional field verifications to ensure the object is correctly set up.
        assertEquals("sender", ShortText.INSTANCE.asString(transfer.sender()));
        assertEquals("target", ShortText.INSTANCE.asString(transfer.target()));
        assertEquals(NanoTime.INSTANCE.parse("2001/02/03T04:05:06.007008009"), transfer.sendingTime());
        assertEquals("Expected account 'from' to match initialised value.", 12345, transfer.from());
        assertEquals("Expected account 'to' to match initialised value.", 67890, transfer.to());
        assertEquals("Expected currency to be 'CURR' after parsing.", "CURR", ShortText.INSTANCE.asString(transfer.currency()));
        assertEquals("Expected amount to be 1.0.", 1.0, transfer.amount(), 0.0);
        assertEquals("Expected reference field to match 'reference' bytes.",
                "reference", transfer.reference().toString());
    }
}
