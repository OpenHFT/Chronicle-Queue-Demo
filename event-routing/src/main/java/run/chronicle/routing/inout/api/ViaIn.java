package run.chronicle.routing.inout.api;

/**
 * This is a ViaIn interface.
 * It provides a generic contract for classes that need to specify a route ("via") and an input source ("in").
 *
 * @param <V> The type of object returned by the 'via' method.
 * @param <I> The type of object returned by the 'in' method.
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
