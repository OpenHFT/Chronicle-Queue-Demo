package run.chronicle.routing.inout.api;

public interface ViaOut<V, O> {
    V via(String name);

    O out();
}
