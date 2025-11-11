/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
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
