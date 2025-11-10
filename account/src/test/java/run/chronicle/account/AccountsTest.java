//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.account;

import com.hubspot.jinjava.Jinjava;
import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.wire.converter.ShortText;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * The {@code AccountsTest} class uses JUnit's Parameterized runner to execute a suite
 * of YAML-driven tests for the Account Management Service (AMS). Each test scenario is
 * defined by YAML files representing inputs and expected outputs. The tests verify that
 * the {@link AccountManagerImpl} responds correctly to various commands, including edge cases
 * introduced via "agitators" that manipulate or remove fields to test the system's resilience.
 *
 * <p>Key Features:
 * <ul>
 *   <li><strong>Parameterized Testing:</strong> Multiple scenarios defined in YAML are run using
 *   the same code, providing comprehensive coverage of both normal and abnormal conditions.</li>
 *   <li><strong>Agitators for Robustness:</strong> Deliberate modifications (e.g., missing fields,
 *   invalid amounts) are introduced to ensure that the system handles validation failures gracefully.</li>
 *   <li><strong>Time Management:</strong> The system clock is controlled via a {@link SetTimeProvider}
 *   to produce deterministic timestamps, making tests reproducible and consistent.</li>
 *   <li><strong>Integration with Jinjava:</strong> Templates containing placeholders (e.g., {{...}})
 *   can be rendered at runtime, enabling dynamic test inputs or scenario generation.</li>
 * </ul>
 */
@SuppressWarnings("deprecation")
@RunWith(Parameterized.class)
public class AccountsTest {

    /**
     * Paths to the YAML test directories. Each directory contains sets of input/output YAML files
     * and may represent different categories of tests (e.g., simple scenarios, mixed scenarios, generated tests).
     */
    static final String paths = "" +
            "account/simple," +
            "account/simple-gen," +
            "account/mixed," +
            "account/waterfall," +
            "account/copilot," +
            "account/gpt-gen," +
            "account/gpt-jinja," +
            "account/bard-gen," +
            "account/bard-jinja," +
            "account/o1-pro";

    /**
     * The identifier ("vault") used as the system ID (target) in tests. This matches the requirements
     * that commands and responses must reference a known target identifier.
     */
    static final long VAULT = ShortText.INSTANCE.parse("vault");

    /**
     * The test name and YamlTester instance for each parameterized test run.
     * <ul>
     *   <li>{@code name} is the scenario name (often derived from the directory name).</li>
     *   <li>{@code tester} is the utility that loads YAML input, runs the {@link AccountManagerImpl},
     *   and compares the actual output against the expected output specified in the YAML files.</li>
     * </ul>
     */
    final String name;
    final net.openhft.chronicle.wire.utils.YamlTester tester;

    /**
     * Constructs a single test parameter instance with a given scenario name and YamlTester.
     *
     * @param name   A descriptive name for the test scenario.
     * @param tester The YamlTester that will execute and verify the test scenario.
     */
    public AccountsTest(String name, net.openhft.chronicle.wire.utils.YamlTester tester) {
        this.name = name;
        this.tester = tester;
    }

    /**
     * Provides a list of test parameters for the Parameterized runner.
     * <p>
     * Uses {@link net.openhft.chronicle.wire.utils.YamlTesterParametersBuilder} to:
     * <ul>
     *   <li>Create an {@link AccountManagerImpl} instance for each scenario.</li>
     *   <li>Introduce "agitators" that simulate invalid or missing data, ensuring robustness and proper validation handling.</li>
     *   <li>Optionally render templates if the YAML files contain Jinjava placeholders.</li>
     * </ul>
     *
     * @return A list of arrays, each containing a scenario name and a YamlTester instance.
     */
    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> parameters() {
        // Returns a list of test parameters to run the tests with.
        // Each test will be run with an instance of AccountManagerImpl,
        // and will be subjected to various agitations to ensure robustness.
        return new net.openhft.chronicle.wire.utils.YamlTesterParametersBuilder<>(out -> new AccountManagerImpl(out).id(VAULT), AccountManagerOut.class, paths)
                // Add agitators to test the system's ability to handle missing or invalid fields.
                .agitators(
                        net.openhft.chronicle.wire.utils.YamlAgitator.messageMissing(),
                        net.openhft.chronicle.wire.utils.YamlAgitator.duplicateMessage(),
                        net.openhft.chronicle.wire.utils.YamlAgitator.overrideFields("currency: , amount: NaN, amount: -1, balance: NaN, balance: -1, target: no-vault".split(", *")),
                        net.openhft.chronicle.wire.utils.YamlAgitator.missingFields("name, account, balance, sender, target, sendingTime, from, to, currency, amount, reference".split(", *")))
                // Use a custom exception handler to ensure JVM errors are logged as events.
                .exceptionHandlerFunction(out -> (log, msg, thrown) -> out.jvmError(thrown == null ? msg : (msg + " " + thrown)))
                .exceptionHandlerFunctionAndLog(true)
                // Render any templates found in the test YAML using Jinjava.
                .inputFunction(s -> s.contains("{{") || s.contains("{#") ? new Jinjava().render(s, Collections.emptyMap()) : s)
                .get();
    }

    /**
     * Reset the system time provider after each test to avoid affecting subsequent tests.
     * Ensures that tests are isolated and no global state "leaks" between them.
     */
    @After
    public void tearDown() {
        SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
    }

    /**
     * Executes the test scenario using the provided YamlTester. Before running the test, sets a known,
     * deterministic time using {@link SetTimeProvider} to ensure reproducible timestamps.
     *
     * <p>The {@code tester.expected()} and {@code tester.actual()} methods are used to compare the
     * expected output with the actual output produced by the AccountManagerImpl.
     *
     * <p>Assertions ensure that the scenario behaves as defined in the YAML specification.
     */
    @Test
    public void runTester() {
        // Set the system clock to a fixed starting time (2023-01-21T11:00:00) and increment by 1 second each event.
        // This gives consistent timestamps for the events, aligning with the requirements.
        SystemTimeProvider.CLOCK = new SetTimeProvider("2023-01-21T11:00:00").autoIncrement(1, TimeUnit.SECONDS);

        // Validate that the actual output matches the expected output defined in the scenario's YAML files.
        // This ensures the AccountManagerImpl logic aligns with the system requirements and handles all specified conditions.
        assertEquals(tester.expected(), tester.actual());
    }
}
