package run.chronicle.routing.inout.api;

public interface ViaIn<V, I> {
    V via(String name);

    I in();
}
