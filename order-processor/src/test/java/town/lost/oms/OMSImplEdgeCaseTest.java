/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package town.lost.oms;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.core.time.TimeProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import town.lost.oms.api.OMSOut;
import town.lost.oms.dto.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * A set of unit tests covering potential edge cases for the OMSImpl class.
 * Although OMSImpl typically relies on a framework to validate inputs,
 * these tests demonstrate how to handle unexpected or invalid DTO values
 * in the context of an event-driven system.
 */
public class OMSImplEdgeCaseTest {

    private OMSOut mockOut;
    private OMSImpl oms;

    @Before
    public void setUp() {
        // Prepare a mock OMSOut so we can verify OMSImpl’s behavior.
        mockOut = mock(OMSOut.class);

        // Create an instance of OMSImpl using the mock OMSOut.
        oms = new OMSImpl(mockOut);

        // Optionally fix the system time so that orderID generation or timestamps are predictable.
        TimeProvider fixedTimeProvider = new SetTimeProvider(1234567890L); // Some fixed nanosecond time
        SystemTimeProvider.CLOCK = fixedTimeProvider;
    }

    @After
    public void tearDown() {
        // Reset the system time provider to the default.
        SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
    }

    /**
     * Test how OMSImpl behaves if a NewOrderSingle has a negative quantity.
     * In a real system, the framework might reject it before calling OMSImpl,
     * but here we simulate a pass-through for edge-case testing.
     */
    @Test
    public void testNewOrderSingleWithNegativeQuantity() {
        // Arrange: create an invalid DTO (negative orderQty).
        NewOrderSingle nos = new NewOrderSingle()
                .sender(1001L)
                .target(2002L)
                .symbol(3003L)
                .orderQty(-100)  // Negative quantity
                .price(1.25)
                .side(Side.BUY)
                .clOrdID("NEGQTY")
                .ordType(OrderType.LIMIT)
                .sendingTime(1234567000L)
                .transactTime(1234567001L);

        // Act: call OMSImpl
        oms.newOrderSingle(nos);

        // Assert: we expect an ExecutionReport to be generated anyway,
        // since OMSImpl doesn’t strictly do its own validation by default.
        // But if you want to simulate a "reject" scenario, you'd adapt the code
        // in OMSImpl or do something like this:
        ArgumentCaptor<ExecutionReport> erCaptor = ArgumentCaptor.forClass(ExecutionReport.class);
        verify(mockOut).executionReport(erCaptor.capture());
        verifyNoMoreInteractions(mockOut);

        ExecutionReport er = erCaptor.getValue();
        assertEquals("NEGQTY", er.clOrdID());
        assertEquals(3003L, er.symbol());
        // Leaves/cumQty are set to 0 by default in OMSImpl
        assertEquals(0.0, er.leavesQty(), 0.0001);
        assertEquals(0.0, er.cumQty(), 0.0001);
        // etc., check any relevant fields
    }

    /**
     * Test behavior if the price is zero or less, which is typically invalid.
     * In a real production environment, you'd likely expect an OrderCancelReject.
     */
    @Test
    public void testNewOrderSingleWithZeroPrice() {
        NewOrderSingle nos = new NewOrderSingle()
                .sender(111L)
                .target(222L)
                .symbol(333L)
                .orderQty(100)
                .price(0.0)  // Zero price
                .side(Side.SELL)
                .clOrdID("ZEROPRICE")
                .ordType(OrderType.MARKET)
                .sendingTime(999999L)
                .transactTime(1000000L);

        oms.newOrderSingle(nos);

        // Verify an ExecutionReport was sent
        ArgumentCaptor<ExecutionReport> erCaptor = ArgumentCaptor.forClass(ExecutionReport.class);
        verify(mockOut).executionReport(erCaptor.capture());
        verifyNoMoreInteractions(mockOut);

        ExecutionReport er = erCaptor.getValue();
        assertEquals("ZEROPRICE", er.clOrdID());
        assertEquals(0.0, er.price(), 0.0001);
    }

    /**
     * Test behavior if the mandatory 'side' field is null.
     * Currently, OMSImpl does not do explicit checks—this test
     * reveals that no rejection occurs unless you code it in.
     */
    @Test
    public void testNewOrderSingleWithMissingSide() {
        NewOrderSingle nos = new NewOrderSingle()
                .sender(123L)
                .target(456L)
                .symbol(789L)
                .orderQty(100)
                .price(1.23)
                .clOrdID("NOSIDE")
                .ordType(OrderType.LIMIT)
                .sendingTime(5000L)
                .transactTime(5001L);
        // side is omitted (null)

        oms.newOrderSingle(nos);

        // By default, OMSImpl does not reject it:
        ArgumentCaptor<ExecutionReport> erCaptor = ArgumentCaptor.forClass(ExecutionReport.class);
        verify(mockOut).executionReport(erCaptor.capture());
    }

    /**
     * Test if cancelOrderRequest is called with a non-existent order ID.
     * The reference OMSImpl simply responds with "No such order."
     */
    @Test
    public void testCancelOrderRequestForUnknownOrder() {
        CancelOrderRequest cor = new CancelOrderRequest()
                .sender(999L)
                .target(888L)
                .symbol(777L)
                .account(12345L)
                .clOrdID("NON_EXISTENT")
                .origClOrdID("OLD_ID")
                .side(Side.BUY)
                .sendingTime(1234000L);

        oms.cancelOrderRequest(cor);

        // We expect an orderCancelReject
        ArgumentCaptor<OrderCancelReject> ocrCaptor = ArgumentCaptor.forClass(OrderCancelReject.class);
        verify(mockOut).orderCancelReject(ocrCaptor.capture());
        verifyNoMoreInteractions(mockOut);

        OrderCancelReject ocr = ocrCaptor.getValue();
        assertEquals("NON_EXISTENT", ocr.clOrdID());
        assertEquals("No such order", ocr.reason());
    }

    /**
     * Test cancelAll when symbol is 0 or clOrdID is empty.
     * By default, OMSImpl again rejects with "No orders to cancel."
     */
    @Test
    public void testCancelAllWithEmptySymbol() {
        CancelAll ca = new CancelAll()
                .sender(100L)
                .target(200L)
                .symbol(0)         // Invalid symbol
                .clOrdID("")       // Empty client order ID
                .sendingTime(5000L);

        oms.cancelAll(ca);

        // Expect "No orders to cancel"
        ArgumentCaptor<OrderCancelReject> ocrCaptor = ArgumentCaptor.forClass(OrderCancelReject.class);
        verify(mockOut).orderCancelReject(ocrCaptor.capture());
        verifyNoMoreInteractions(mockOut);

        OrderCancelReject ocr = ocrCaptor.getValue();
        assertEquals("", ocr.clOrdID());
        assertEquals("No orders to cancel", ocr.reason());
    }

    /**
     * Demonstrates how an exception might be handled, even though OMSImpl
     * does not currently throw any by default.
     */
    @Test
    public void testExceptionHandling() {
        // Mock OMSOut to do something unexpected
        doThrow(new RuntimeException("Simulated Crash"))
                .when(mockOut)
                .executionReport(any(ExecutionReport.class));

        NewOrderSingle nos = new NewOrderSingle()
                .sender(1)
                .target(2)
                .symbol(3)
                .orderQty(100)
                .price(123.45)
                .side(Side.BUY)
                .clOrdID("EXCEPTION")
                .ordType(OrderType.MARKET)
                .sendingTime(10)
                .transactTime(11);

        // Try-catch block to demonstrate how we'd detect an exception inside the test
        try {
            oms.newOrderSingle(nos);
        } catch (RuntimeException e) {
            // We can verify jvmError was called, if OMSImpl had that logic directly.
            // Or handle it here if we want. For now, just confirm the message.
            assertEquals("Simulated Crash", e.getMessage());
        }
    }
}
