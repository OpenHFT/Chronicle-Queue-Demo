/*
 * Copyright 2016-2025 chronicle.software
 */
package town.lost.oms;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import town.lost.oms.api.OMSOut;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Test class for OMSImpl.
 * The OMSImplTest runs tests for each method in OMSImpl class.
 * The test data is read from specified files and the actual output is compared against expected output.
 */
@SuppressWarnings("deprecation")
@RunWith(Parameterized.class)
public class OMSImplTest {
    // Defines the paths to the tests to run.
    static final List<String> paths = Arrays.asList(new String[]{
            "newOrderSingle",
            "newOrderSingleEquity",
            "cancelOrderRequest",
            "cancelAll"
    });

    // The name of the test, and the tester that will run the test.
    final String name;
    final net.openhft.chronicle.wire.utils.YamlTester tester;

    // Constructor that sets the name and tester.
    public OMSImplTest(String name, net.openhft.chronicle.wire.utils.YamlTester tester) {
        this.name = name;
        this.tester = tester;
    }

    // Defines the parameters for the parameterized test runner.
    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> parameters() {
        // Returns a list of test parameters to run the tests with.
        // Each test will be run with an instance of AccountManagerImpl,
        // and will be subjected to various agitations to ensure robustness.
        return new net.openhft.chronicle.wire.utils.YamlTesterParametersBuilder<>(out -> new OMSImpl(out), OMSOut.class, paths)
                .agitators(
                        net.openhft.chronicle.wire.utils.YamlAgitator.messageMissing(),
                        net.openhft.chronicle.wire.utils.YamlAgitator.duplicateMessage(),
                        net.openhft.chronicle.wire.utils.YamlAgitator.overrideFields("sendingTime: '', symbol: '', side: '', orderQty: NaN, orderQty: -1, price: NaN, price: -1, clOrdID: '', ordType: ''".split(", *")),
                        net.openhft.chronicle.wire.utils.YamlAgitator.missingFields("sender, target, sendingTime, symbol, transactTime, account, orderQty, price, side, clOrdID, ordType, timeInForce, currency".split(", *")))
                .exceptionHandlerFunction(out -> (log, msg, thrown) -> out.jvmError(thrown == null ? msg : (msg + " " + thrown)))
                .exceptionHandlerFunctionAndLog(true)
                .get();
    }

    // After each test, this method resets the system time provider.
    @After
    public void tearDown() {
        SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
    }

    // This is the actual test method, which uses the provided tester
    // to run the test and then compares the expected output to the actual output.
    @Test
    public void runTester() {
        // Sets the system clock to a specific time for the purpose of testing.
        SystemTimeProvider.CLOCK = new SetTimeProvider("2019-12-03T09:54:37.345679")
                .autoIncrement(1, TimeUnit.SECONDS);
        // Asserts that the expected output matches the actual output.
        assertEquals(tester.expected(), tester.actual());
    }
}
