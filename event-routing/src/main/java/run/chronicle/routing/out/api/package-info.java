/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
/**
 * This package, run.chronicle.routing.out.api, contains interfaces and classes that facilitate
 * the routing and processing of messages in the output part of the system.
 *
 * This includes:
 *
 * - AllOut: An interface providing a contract for any class that wants to send a value message using the 'value' method.
 * - Even: A class that encapsulates a long value and provides methods for setting and retrieving this value.
 * - EvenIn: An interface providing a contract for classes that need to process an Even object.
 * - SifterIn: An interface providing a contract for any class that needs to process a Value object.
 * - SifterOut: An interface providing a contract for classes that manage the output to various destinations.
 * - Triple: A class that encapsulates a long value, providing methods for setting and retrieving this value.
 * - TripleIn: An interface providing a contract for any class that needs to process a Triple object.
 *
 * The goal of this package is to provide a flexible and extensible means of routing and processing messages
 * in the output part of the system.
 */
package run.chronicle.routing.out.api;
