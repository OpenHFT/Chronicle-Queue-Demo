package event.driven.program;

import event.driven.program.api.Says;
import net.openhft.chronicle.wire.Wires;

import java.io.IOException;

/**
 * This is a RecordInputAsYamlMain class.
 * It reads lines of text from the standard input and forwards them to a YAML-formatted logger, which writes the interactions to the console.
 * This class leverages the {@link Wires} utility class to create a proxy that records method calls in YAML format.
 *
 * @since 2023-07-30
 */
public class RecordInputAsYamlMain {
    /**
     * This method obtains a proxy of the {@link Says} interface that writes method calls and their arguments in YAML format
     * to the given PrintStream (in this case, standard output). It takes each line of input from the user and calls the {@code say} method
     * each time, allowing the says proxy to log the call in YAML format.
     *
     * @param args Command-line arguments, not used in this implementation.
     * @throws IOException If an I/O error occurs while reading from the standard input.
     */
    public static void main(String[] args) throws IOException {
        // Obtains a proxy of the Says interface that writes method calls and their arguments
        // in YAML format to the given PrintStream (in this case, standard output).
        final Says says = Wires.recordAsYaml(Says.class, System.out);

        // Takes each line of input from the user and calls the say(theLine) method each time,
        // allowing the says proxy to log the call in YAML format.
        SaysInput.input(says);
    }
}

