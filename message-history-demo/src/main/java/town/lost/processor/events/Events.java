package town.lost.processor.events;

import net.openhft.chronicle.bytes.MethodId;

public interface Events {
    @MethodId(1)
    void eventOne(EventOne one);

    @MethodId(2)
    void eventTwo(EventTwo two);
}
