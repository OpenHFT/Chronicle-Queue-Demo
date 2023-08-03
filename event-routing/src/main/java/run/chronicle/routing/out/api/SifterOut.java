package run.chronicle.routing.out.api;

/**
 * This is a SifterOut interface.
 * It provides a contract for classes that need to manage the output to various destinations.
 * It has methods for obtaining output routes for 'All', 'Even', and 'Triple' type messages.
 */
public interface SifterOut {

    /**
     * This method provides a contract for getting the output route for 'All' type messages.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @return An AllOut object which represents the output route for 'All' type messages
     */
    AllOut out();

    /**
     * This method provides a contract for getting the output route for 'Even' type messages.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @return An EvenIn object which represents the output route for 'Even' type messages
     */
    EvenIn evens();

    /**
     * This method provides a contract for getting the output route for 'Triple' type messages.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @return A TripleIn object which represents the output route for 'Triple' type messages
     */
    TripleIn triples();
}
