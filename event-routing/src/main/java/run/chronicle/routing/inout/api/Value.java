package run.chronicle.routing.inout.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

/**
 * Represents a simple DTO with a numeric value 'val'.
 * We can route these messages based on val's properties
 * (e.g., divisible by 3, negative, etc.).
 */
public class Value extends SelfDescribingMarshallable {

    // This is the encapsulated long value
    private long val;

    /**
     * This is a constructor for the Value class.
     * It initialises the val instance variable with the provided value.
     *
     * @param val A long value to set as the initial value for this instance
     */
    public Value(long val) {
        this.val = val;
    }

    /**
     * Returns the numeric value of this message.
     */
    public long val() {
        return val;
    }

    /**
     * (Optional) Allows updating the numeric value.
     */
    public Value val(long newVal) {
        this.val = newVal;
        return this;
    }
}
