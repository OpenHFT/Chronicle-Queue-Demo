package run.chronicle.routing;

import net.openhft.chronicle.bytes.UpdateInterceptor;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.Closeable;
import net.openhft.chronicle.wire.*;

import java.util.function.Supplier;

public final class ViaOutTextMethodWriter implements ViaOut<ValueMessage, ValueMessage>, ValueMessage, MethodWriter {

    // result
    private transient final Closeable closeable;
    private transient Supplier<MarshallableOut> out;

    // constructor
    public ViaOutTextMethodWriter(Supplier<MarshallableOut> out, Closeable closeable, UpdateInterceptor updateInterceptor) {
        this.out = out;
        this.closeable = closeable;
    }

    @Override
    public void marshallableOut(MarshallableOut out) {
        this.out = () -> out;
    }

    public ValueMessage out() {
        try (final WriteDocumentContext dc = (WriteDocumentContext) this.out.get().acquireWritingDocument(false)) {
            try {
                dc.chainedElement(true);
                if (out.get().recordHistory()) MessageHistory.writeHistory(dc);
                final ValueOut valueOut = dc.wire().writeEventName("out");
                valueOut.text("");
            } catch (Throwable t) {
                dc.rollbackOnClose();
                throw Jvm.rethrow(t);
            }
        }
        return this;
    }

    public ValueMessage via(final String arg0) {
        try (final WriteDocumentContext dc = (WriteDocumentContext) this.out.get().acquireWritingDocument(false)) {
            try {
                dc.chainedElement(true);
                if (out.get().recordHistory()) MessageHistory.writeHistory(dc);
                final ValueOut valueOut = dc.wire().writeEventName("via");
                valueOut.text(arg0);
            } catch (Throwable t) {
                dc.rollbackOnClose();
                throw Jvm.rethrow(t);
            }
        }
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
