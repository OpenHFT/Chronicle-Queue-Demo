/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
/**
 * This package, run.chronicle.routing.inout, includes classes related to handling, routing, and processing 
 * messages from multiple sources and routing them to multiple destinations.
 *
 * <p>This package includes:</p>
 * <ul>
 *   <li>{@link run.chronicle.routing.inout.ViaThreeFive}: Routes {@link run.chronicle.routing.inout.api.ValueMessage} messages
 *       from multiple sources to multiple destinations. Implements {@link run.chronicle.routing.inout.api.ViaIn}
 *       and {@link run.chronicle.routing.inout.api.ValueMessage}, providing functionality for named routing paths
 *       and handling of {@code ValueMessage} objects.</li>
 * </ul>
 *
 * - ViaThreeFive: A class that takes messages from multiple sources and routes them to multiple sources. 
 *   This class implements the ViaIn&lt;ValueMessage, ValueMessage&gt; and ValueMessage interfaces,
 *   providing functionality for a named routing path and the handling of ValueMessage objects.
 *
 * The goal of this package is to provide an efficient and extensible framework for managing the flow of 
 * messages in a system with multiple inputs and outputs.
 *
 * @since 2023-07-29
 */
package run.chronicle.routing.inout;
