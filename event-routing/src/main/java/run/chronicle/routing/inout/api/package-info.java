/**
 * Provides interfaces and classes for routing messages in and out using a Chronicle-based infrastructure.
 *
 * <p>This package includes:</p>
 * <ul>
 *   <li>{@link Value}: A class that encapsulates a long value, providing methods for retrieval.</li>
 *   <li>{@link ValueMessage}: An interface defining a contract for sending {@code Value} messages.</li>
 *   <li>{@link ViaIn}: An interface for specifying a route ("via") and obtaining an input source ("in").</li>
 *   <li>{@link ViaOut}: An interface for specifying a route ("via") and obtaining an output destination ("out").</li>
 * </ul>
 *
 * <p>The goal of this package is to offer a flexible and extensible means of routing messages in various formats and protocols, supporting different input/output strategies.</p>
 *
 * @since 2023-07-29
 */
package run.chronicle.routing.inout.api;

