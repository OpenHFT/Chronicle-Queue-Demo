package run.chronicle.routing.inout;

import run.chronicle.routing.inout.api.Value;
import run.chronicle.routing.inout.api.ValueMessage;
import run.chronicle.routing.inout.api.ViaIn;
import run.chronicle.routing.inout.api.ViaOut;

/**
 * Takes messages from multiple sources and routes to multiple sources
 */
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
            if (value.value() % 3 == 0)
                out.via("three")
                        .value(value);
            if (value.value() % 5 == 0)
                out.via("five")
                        .value(value);
        }
        out.out()
                .value(value);
    }
}
