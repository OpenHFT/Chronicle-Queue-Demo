package run.chronicle.routing.inout.api;

import net.openhft.chronicle.bytes.UpdateInterceptor;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.wire.*;

import java.util.function.Supplier;

// This class implements the ViaOut, ValueMessage, and MethodWriter interfaces.
// It handles the output of ValueMessage objects using a MethodWriter.
public final class ViaOutTextMethodWriter implements ViaOut<ValueMessage, ValueMessage>, ValueMessage, MethodWriter {

    // Closeable resource which needs to be released when done
    private transient final Closeable closeable;

    // Supplier of MarshallableOut, to provide output handling mechanism
    private transient Supplier<MarshallableOut> out;

    // Constructor for the ViaOutTextMethodWriter class.
    // It initializes the Supplier<MarshallableOut> for output handling and a Closeable resource to be managed.
    // An UpdateInterceptor is also accepted as an argument, but currently not used in the class.
    public ViaOutTextMethodWriter(Supplier<MarshallableOut> out, Closeable closeable, UpdateInterceptor updateInterceptor) {
        this.out = out;
        this.closeable = closeable;
    }

    @Override
    public void marshallableOut(MarshallableOut out) {
        this.out = () -> out;
    }

    /**
     * This method acquires a writing document from the output Supplier and performs various operations on it.
     * If any error occurs during the process, the method will rollback and rethrow the exception.
     *
     * @return Returns the current instance after performing the operations.
     */
    public ValueMessage out() {
        // Acquire a writing document from the output Supplier
        try (final WriteDocumentContext dc = (WriteDocumentContext) this.out.get().acquireWritingDocument(false)) {
            try {
                // Set the chainedElement flag to true
                dc.chainedElement(true);

                // If the output record history is enabled, write the history to the document
                if (out.get().recordHistory()) MessageHistory.writeHistory(dc);

                // Write an event named "out" to the wire of the document
                final ValueOut valueOut = dc.wire().writeEventName("out");

                // Write an empty text to the value out
                valueOut.text("");
            } catch (Throwable t) {
                // If any error occurs, rollback the document and rethrow the exception
                dc.rollbackOnClose();
                throw Jvm.rethrow(t);
            }
        }

        // Return the current instance
        return this;
    }


    /**
     * This method acquires a writing document from the output Supplier and performs various operations on it.
     * If any error occurs during the process, the method will rollback and rethrow the exception.
     *
     * @param arg0 The text to be written to the ValueOut
     * @return Returns the current instance after performing the operations.
     */
    public ValueMessage via(final String arg0) {
        // Acquire a writing document from the output Supplier
        try (final WriteDocumentContext dc = (WriteDocumentContext) this.out.get().acquireWritingDocument(false)) {
            try {
                // Set the chainedElement flag to true
                dc.chainedElement(true);

                // If the output record history is enabled, write the history to the document
                if (out.get().recordHistory()) MessageHistory.writeHistory(dc);

                // Write an event named "via" to the wire of the document
                final ValueOut valueOut = dc.wire().writeEventName("via");

                // Write the given text to the value out
                valueOut.text(arg0);
            } catch (Throwable t) {
                // If any error occurs, rollback the document and rethrow the exception
                dc.rollbackOnClose();
                throw Jvm.rethrow(t);
            }
        }

        // Return the current instance
        return this;
    }

    @Override
    public void value(Value value) {
        try (final WriteDocumentContext dc = (WriteDocumentContext) this.out.get().acquireWritingDocument(false)) {
            try {
                dc.chainedElement(false);
                if (out.get().recordHistory()) MessageHistory.writeHistory(dc);
                final ValueOut valueOut = dc.wire().writeEventName("value");
                valueOut.object(Value.class, value);
            } catch (Throwable t) {
                dc.rollbackOnClose();
                throw Jvm.rethrow(t);
            }
        }
    }
}
