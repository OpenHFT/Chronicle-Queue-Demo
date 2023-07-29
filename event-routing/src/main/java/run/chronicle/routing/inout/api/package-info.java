/**
 * This package, run.chronicle.routing.inout.api, contains interfaces and classes that provide
 * a foundation for routing messages in and out using a Chronicle-based infrastructure.
 *
 * This includes:
 *
 * - Value: A class that wraps a long value and provides methods for setting and retrieving this value.
 * - ValueMessage: An interface that provides a contract for any class that wants to send a value message.
 * - ViaIn: An interface that provides a generic contract for classes that need to specify a route ("via") and an input source ("in").
 * - ViaOut: An interface that provides a generic contract for classes that need to specify a route ("via") and an output destination ("out").
 *
 * The goal of this package is to provide a flexible and extensible means of routing messages in 
 * various formats and protocols, supporting different input/output strategies.
 *
 * @since 2023-07-29
 */
package run.chronicle.routing.inout.api;
