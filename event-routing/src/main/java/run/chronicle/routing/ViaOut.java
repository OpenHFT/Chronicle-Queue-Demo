package run.chronicle.routing;

public interface ViaOut<V, O> {
    V via(String name);

    O out();
}
