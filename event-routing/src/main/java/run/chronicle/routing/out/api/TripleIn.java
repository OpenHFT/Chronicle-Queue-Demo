package run.chronicle.routing.out.api;

/**
 * This is a TripleIn interface.
 * It provides a contract for any class that needs to process a Triple object.
 * The purpose of this interface is to encapsulate the operation on a Triple object.
 *
 * @since 2023-07-29
 */
public interface TripleIn {

    /**
     * This method provides a contract for processing a Triple object.
     * Any class implementing this interface will need to provide its own implementation of this method.
     *
     * @param triple A Triple object to be processed
     */
    void triple(Triple triple);
}
