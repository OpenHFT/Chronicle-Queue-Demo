package run.chronicle.routing.inout;

import run.chronicle.routing.inout.api.Value;
import run.chronicle.routing.inout.api.ValueMessage;
import run.chronicle.routing.inout.api.ViaIn;
import run.chronicle.routing.inout.api.ViaOut;

/**
 * Takes messages from multiple sources and routes to multiple sources
 */

public class ViaThreeFive implements ViaIn<ValueMessage, ValueMessage>, ValueMessage {

    // The instance of ViaOut interface which represents the output destination for messages
    final ViaOut<ValueMessage, ValueMessage> out;

    // The name of this instance of ViaThreeFive. Can be used for identification or routing purposes
    String name;

    /**
     * This is the constructor for ViaThreeFive class.
     * It initializes the 'out' instance variable with the provided ViaOut instance.
     *
     * @param out A ViaOut instance that will be used as the output destination for messages
     */
    public ViaThreeFive(ViaOut<ValueMessage, ValueMessage> out) {
        this.out = out;
    }

    // Implementation of ViaIn and ValueMessage methods should be here

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
            if (value.val() % 3 == 0)
                out.via("three")
                        .value(value);
            if (value.val() % 5 == 0)
                out.via("five")
                        .value(value);
        }
        out.out()
                .value(value);
    }
}
