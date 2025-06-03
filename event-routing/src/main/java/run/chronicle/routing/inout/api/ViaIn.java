package run.chronicle.routing.inout.api;

/**
 * An interface for specifying a route ("via") and obtaining
 * an input source ("in").
 *
 * Typically, you'd call via("someName") to define a route,
 * then in() to start reading from that route as a ValueMessage.
 *
 * @param <V> the type returned by the {@code via} method
 * @param <I> the type returned by the {@code in} method
 */
public interface ViaIn<V, I> {

    /**
     * This method is expected to be implemented to provide a specific route or way.
     *
     * @param name The name of the route or way.
     * @return An instance of type V representing the specific route or way.
     */
    V via(String name);

    /**
     * This method is expected to be implemented to provide an input source.
     *
     * @return An instance of type I representing the input source.
     */
    I in();
}
