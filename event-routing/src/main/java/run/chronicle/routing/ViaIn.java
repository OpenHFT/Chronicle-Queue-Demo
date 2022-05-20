package run.chronicle.routing;

public interface ViaIn<V, I> {
    V via(String name);

    I in();
}
