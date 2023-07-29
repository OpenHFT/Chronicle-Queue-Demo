package run.chronicle.routing.inout.api;

/**
 * This is a ViaOut interface.
 * It provides a generic contract for classes that need to specify a route ("via") and an output destination ("out").
 *
 * @param <V> The type of object returned by the 'via' method.
 * @param <O> The type of object returned by the 'out' method.
 *
 * @since 2023-07-29
 */
public interface ViaOut<V, O> {

    /**
     * This method is expected to be implemented to provide a specific route or path.
     *
     * @param name The name of the route or path.
     * @return An instance of type V representing the specific route or path.
     */
    V via(String name);

    /**
     * This method is expected to be implemented to provide an output destination.
     *
     * @return An instance of type O representing the output destination.
     */
    O out();
}

