package town.lost.processor.events;

import net.openhft.chronicle.wire.VanillaMessageHistory;

interface EventWithHistory extends Events {
    EventWithHistory history(VanillaMessageHistory mh);
}
