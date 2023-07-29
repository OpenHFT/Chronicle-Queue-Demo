package run.chronicle.routing.inout.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

/**
 * This is a Value class extending SelfDescribingMarshallable.
 * It wraps a long value and provides methods for setting and retrieving this value.
 *
 * @since 2023-07-29
 */
public class Value extends SelfDescribingMarshallable {

    // This is the encapsulated long value
    private long val;

    /**
     * This is a constructor for the Value class.
     * It initializes the val instance variable with the provided value.
     *
     * @param val A long value to set as the initial value for this instance
     */
    public Value(long val) {
        this.val = val;
    }

    /**
     * This is a getter for the val instance variable.
     * It returns the current long value of this Value object.
     *
     * @return The current long value of this Value object
     */
    public long val() {
        return val;
    }
}
