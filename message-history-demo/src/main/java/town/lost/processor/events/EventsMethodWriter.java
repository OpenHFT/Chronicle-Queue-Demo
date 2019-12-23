package town.lost.processor.events;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.annotation.UsedViaReflection;
import net.openhft.chronicle.wire.MethodWriterInvocationHandlerSupplier;

import java.lang.reflect.Method;
import java.util.stream.IntStream;

@UsedViaReflection
public class EventsMethodWriter implements town.lost.processor.events.Events,
        net.openhft.chronicle.core.io.Closeable {

    private static final int maxArgs = 1;
    private final MethodWriterInvocationHandlerSupplier handler;
    private final Method[] methods = new Method[3];
    private final ThreadLocal<Object[][]> argsTL = ThreadLocal.withInitial(() -> IntStream.range(0, maxArgs + 1).mapToObj(Object[]::new).toArray(Object[][]::new));

    public EventsMethodWriter(MethodWriterInvocationHandlerSupplier handler) {
        this.handler = handler;

        //  public void eventOne(town.lost.processor.events.EventOne arg0) {
        methods[0] = town.lost.processor.events.Events.class.getMethods()[0];

        //  public void eventTwo(town.lost.processor.events.EventTwo arg0) {
        methods[1] = town.lost.processor.events.Events.class.getMethods()[1];

        //  public void close() {
        methods[2] = net.openhft.chronicle.core.io.Closeable.class.getMethods()[4];
    }

    public void eventOne(town.lost.processor.events.EventOne arg0) {
        Method _method_ = this.methods[0];
        Object[] _a_ = this.argsTL.get()[1];
        _a_[0] = arg0;
        try {
            handler.get().invoke(this, _method_, _a_);
        } catch (Throwable throwable) {
            throw Jvm.rethrow(throwable);
        }
    }

    public void eventTwo(town.lost.processor.events.EventTwo arg0) {
        Method _method_ = this.methods[1];
        Object[] _a_ = this.argsTL.get()[1];
        _a_[0] = arg0;
        try {
            handler.get().invoke(this, _method_, _a_);
        } catch (Throwable throwable) {
            throw Jvm.rethrow(throwable);
        }
    }

    public void close() {
        Method _method_ = this.methods[2];
        Object[] _a_ = this.argsTL.get()[0];
        try {
            handler.get().invoke(this, _method_, _a_);
        } catch (Throwable throwable) {
            throw Jvm.rethrow(throwable);
        }
    }
}
