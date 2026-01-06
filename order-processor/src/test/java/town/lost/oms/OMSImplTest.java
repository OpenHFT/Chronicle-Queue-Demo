/*
 * Copyright 2016-2025 chronicle.software
 */
package town.lost.oms;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import town.lost.oms.api.OMSOut;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for OMSImpl.
 * The OMSImplTest runs tests for each method in OMSImpl class.
 * The test data is read from specified files and the actual output is compared against expected output.
 */
@SuppressWarnings("deprecation")
public class OMSImplTest {
    // Defines the paths to the tests to run.
    static final List<String> paths = Arrays.asList(new String[]{
            "newOrderSingle",
            "newOrderSingleEquity",
            "cancelOrderRequest",
            "cancelAll"
    });

    // Defines the parameters for the parameterized test runner.
    public static Stream<Arguments> parameters() {
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
                .get()
                .stream()
                .map(params -> Arguments.of((String) params[0], (net.openhft.chronicle.wire.utils.YamlTester) params[1]));
    }

    // After each test, this method resets the system time provider.
    @AfterEach
    public void tearDown() {
        SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
    }

    // This is the actual test method, which uses the provided tester
    // to run the test and then compares the expected output to the actual output.
    @ParameterizedTest(name = "{0}")
    @MethodSource("parameters")
    public void runTester(String name, net.openhft.chronicle.wire.utils.YamlTester tester) {
        // Sets the system clock to a specific time for the purpose of testing.
        SystemTimeProvider.CLOCK = new SetTimeProvider("2019-12-03T09:54:37.345679")
                .autoIncrement(1, TimeUnit.SECONDS);
        // Asserts that the expected output matches the actual output.
        assertEquals(tester.expected(), tester.actual(), () -> "OMSImpl YAML scenario=" + name);
    }
}
