package run.chronicle.routing.inout.api;

/**
 * This is a ViaOut interface.
 * It provides a generic contract for classes that need to specify a route ("via") and an output destination ("out").
 *
 * @param <V> the type returned by the {@code via} method
 * @param <O> the type returned by the {@code out} method
 */
public interface ViaOut<V, O> {

    /**
     * This method is expected to be implemented to provide a specific route or path.
     *
     * @param name the name of the route
     * @return an instance of type {@code V} representing the specified route
     */
    V via(String name);

    /**
     * This method is expected to be implemented to provide an output destination.
     *
     * @return an instance of type {@code O} representing the output destination
     */
    O out();
}

