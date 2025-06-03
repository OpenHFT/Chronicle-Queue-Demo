package town.lost.oms.api;

/**
 * The {@code ErrorListener} interface defines how critical errors or exceptions
 * are communicated back to the system. This is conceptually similar to capturing
 * unexpected events in a FIX flow (e.g., invalid data or system faults) and passing
 * them upstream to a monitoring or logging component.
 * <p>
 * In a FIX-based system, errors might also be reflected in Reject messages or
 * {@code jvmError(msg)} calls if the framework intercepts an unhandled exception.
 */
public interface ErrorListener {

    /**
     * Called when any critical system error occurs.
     *
     * @param msg A descriptive message about the JVM error or exception.
     */
    void jvmError(String msg);
}
