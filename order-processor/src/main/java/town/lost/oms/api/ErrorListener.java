package town.lost.oms.api;

/**
 * This interface provides a method to handle JVM errors.
 * Implementations of this interface will define how these errors are handled.
 */
public interface ErrorListener {

    /**
     * This method handles JVM errors.
     * It takes a String message which provides details about the error.
     *
     * @param msg a String providing details about the JVM error.
     */
    void jvmError(String msg);
}