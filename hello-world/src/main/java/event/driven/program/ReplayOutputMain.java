package event.driven.program;

import net.openhft.chronicle.wire.Wires;

import java.io.IOException;

public class ReplayOutputMain {
    public static void main(String[] args) throws IOException {
        // Reads the content of a Yaml file specified in args[0] and feeds it to SaysOutput.
        Wires.replay(args[0], new SaysOutput());
    }
}
