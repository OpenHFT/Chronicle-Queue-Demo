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
package run.chronicle.account.util;

/**
 * A functional interface for handling critical JVM-level errors in the system.
 * Implementations can define custom error-handling strategies such as logging,
 * triggering alerts, or performing cleanup and shutdown procedures.
 * <p>
 * Using a replicated queue, this can be done on another machine to avoid
 * the impact of GC or IO the monitoring system might have.
 *
 * <p>Example usage:
 * <pre>
 * public class LoggingErrorListener implements ErrorListener {
 *     public void jvmError(String msg) {
 *         // Log the error to monitoring system, a file, or console
 *         logger.error(msg);
 *     }
 * }</pre>
 *
 * <p>By providing different {@code ErrorListener} implementations, the system
 * can adapt to various environments (production vs. development) or integrate
 * with different error-handling frameworks.
 */
@FunctionalInterface
public interface ErrorListener {

    /**
     * Handles a critical JVM-level error.
     * <p>
     * Implementations may choose to:
     * <ul>
     *   <li>Log the error message to files or monitoring systems.</li>
     *   <li>Send alerts to administrators.</li>
     *   <li>Trigger a controlled shutdown or cleanup process.</li>
     * </ul>
     *
     * @param msg a human-readable message providing details about the encountered JVM error.
     */
    void jvmError(String msg);
}
