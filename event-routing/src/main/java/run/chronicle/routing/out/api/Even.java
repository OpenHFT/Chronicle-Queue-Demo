package run.chronicle.routing.out.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

/**
 * This is the Even class extending SelfDescribingMarshallable.
 * It encapsulates a long value providing methods for setting and retrieving this value.
 * The class is designed to follow the builder pattern with the 'val' method returning the current instance.
 */
public class Even extends SelfDescribingMarshallable {

    // This is the encapsulated long value
    private long val;

    /**
     * This is a getter for the val instance variable.
     * It returns the current long value of this Even object.
     *
     * @return The current long value of this Even object
     */
    public long val() {
        return val;
    }

    /**
     * This is a setter for the val instance variable.
     * It sets the provided long value to the val instance variable and returns the current instance.
     * This method is part of the builder pattern allowing chained method calls.
     *
     * @param value The new long value to be set
     * @return The current instance of the Even class
     */
    public Even val(long value) {
        this.val = value;
        return this;
    }
}
