package event.driven.program;

import event.driven.program.api.Says;
import net.openhft.chronicle.wire.Wires;

import java.io.IOException;

public class RecordInputAsYamlMain {
    public static void main(String[] args) throws IOException {
        // obtains a proxy that writes to the PrintStream the method calls and their arguments
        final Says says = Wires.recordAsYaml(Says.class, System.out);
        // Takes each line input and calls say(theLine) each time
        SaysInput.input(says);
    }
}
