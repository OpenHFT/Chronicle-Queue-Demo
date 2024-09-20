package run.chronicle.routing.inout;

import run.chronicle.routing.inout.api.Value;
import run.chronicle.routing.inout.api.ValueMessage;
import run.chronicle.routing.inout.api.ViaIn;
import run.chronicle.routing.inout.api.ViaOut;

/**
 * Routes {@link Value} messages from multiple sources to multiple destinations based on specific criteria.
 *
 * <p>This class implements {@link ViaIn} and {@link ValueMessage} to process incoming {@code Value} messages.
 * It checks if the name contains the character 'e', and if so, routes the value to different
 * destinations depending on whether the value is divisible by 3 or 5.</p>
 */
public class ViaThreeFive implements ViaIn<ValueMessage, ValueMessage>, ValueMessage {

    // The instance of ViaOut interface which represents the output destination for messages
    final ViaOut<ValueMessage, ValueMessage> out;

    // The name of this instance of ViaThreeFive. Can be used for identification or routing purposes
    String name;

    /**
     * Constructs a new {@code ViaThreeFive} instance with the specified output destination.
     *
     * @param out the {@link ViaOut} instance used as the output destination for messages
     */
    public ViaThreeFive(ViaOut<ValueMessage, ValueMessage> out) {
        this.out = out;
    }

    /**
     * Specifies a route or pathway identified by the given name.
     *
     * @param name the name of the route
     * @return this {@code ViaThreeFive} instance for method chaining
     */
    @Override
    public ValueMessage via(String name) {
        this.name = name;
        return this;
    }

    /**
     * Resets the routing name to an empty string.
     *
     * @return this {@code ViaThreeFive} instance for method chaining
     */
    @Override
    public ValueMessage in() {
        this.name = "";
        return this;
    }

    /**
     * Processes the given {@link Value} message and routes it to appropriate destinations.
     * If the name contains the character 'e', it checks if the value is divisible by 3 or 5
     * and routes accordingly.
     *
     * @param value the {@link Value} message to process
     */
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
