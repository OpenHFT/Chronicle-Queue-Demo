/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package town.lost.processor.events;

import net.openhft.chronicle.wire.VanillaMessageHistory;

interface EventWithHistory extends Events {
    EventWithHistory history(VanillaMessageHistory mh);
}
