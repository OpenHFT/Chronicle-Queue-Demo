package run.chronicle.routing;

public class ViaThreeFive implements ViaIn<ValueMessage, ValueMessage>, ValueMessage {
    final ViaOut<ValueMessage, ValueMessage> out;
    String name;

    public ViaThreeFive(ViaOut<ValueMessage, ValueMessage> out) {
        this.out = out;
    }

    @Override
    public ValueMessage via(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ValueMessage in() {
        this.name = "";
        return this;
    }

    @Override
    public void value(Value value) {
        if (name.contains("e")) {
            if (value.value % 3 == 0)
                out.via("three")
                        .value(value);
            if (value.value % 5 == 0)
                out.via("five")
                        .value(value);
        }
        out.out()
                .value(value);
    }
}
