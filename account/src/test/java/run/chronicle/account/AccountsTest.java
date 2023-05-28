/*
 * Copyright 2016-2022 chronicle.software
 *
 *       https://chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package run.chronicle.account;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.utils.YamlAgitator;
import net.openhft.chronicle.wire.utils.YamlTester;
import net.openhft.chronicle.wire.utils.YamlTesterParametersBuilder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import run.chronicle.account.api.AccountManagerOut;
import run.chronicle.account.impl.AccountManagerImpl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * This class AccountsTest is a test class that uses
 * JUnit's Parameterized runner to run multiple tests with different parameters.
 * The test parameters are set up in the parameters method
 * and are used to create an instance of YamlTester for each test.
 * Each test runs through the runTester method which sets the system clock to a specific time,
 * runs the test, and checks the output.
 * After each test, the system clock is reset to its default state in the tearDown method.
 */
// This class is used to run tests for the Account system.
@RunWith(Parameterized.class)
public class AccountsTest {
    // Defines the paths to the tests to run.
    static final String paths = "" +
            "account/simple," +
            "account/mixed," +
            "account/waterfall," +
            "account/gpt-gen";
    static final long VAULT = Base85.INSTANCE.parse("vault");

    // The name of the test, and the tester that will run the test.
    final String name;
    final YamlTester tester;

    // Constructor that sets the name and tester.
    public AccountsTest(String name, YamlTester tester) {
        this.name = name;
        this.tester = tester;
    }

    // Defines the parameters for the parameterized test runner.
    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> parameters() {
        // Returns a list of test parameters to run the tests with.
        // Each test will be run with an instance of AccountManagerImpl,
        // and will be subjected to various agitations to ensure robustness.
        return new YamlTesterParametersBuilder<>(out -> new AccountManagerImpl(out).id(VAULT), AccountManagerOut.class, paths)
                .agitators(
                        YamlAgitator.messageMissing(),
                        YamlAgitator.duplicateMessage(),
                        YamlAgitator.overrideFields("currency: , amount: NaN, amount: -1, balance: NaN, balance: -1, target: no-vault".split(", *")),
                        YamlAgitator.missingFields("name, account, balance, sender, target, sendingTime, from, to, currency, amount, reference".split(", *")))
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
        SystemTimeProvider.CLOCK = new SetTimeProvider("2023-01-20T10:10:00")
                .autoIncrement(1, TimeUnit.MILLISECONDS);
        // Asserts that the expected output matches the actual output.
        assertEquals(tester.expected(), tester.actual());
    }
}
