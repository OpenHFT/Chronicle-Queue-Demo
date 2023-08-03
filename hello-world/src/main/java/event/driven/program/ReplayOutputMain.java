package event.driven.program;

import net.openhft.chronicle.wire.Wires;

import java.io.IOException;

/**
 * This is a ReplayOutputMain class.
 * It reads the content of a specified YAML file and replays it using the {@link SaysOutput} class, printing the interactions to the console.
 */
public class ReplayOutputMain {
    /**
     * This method reads the content of a YAML file specified by the first command-line argument (args[0])
     * and feeds it to a {@link SaysOutput} object, which handles printing the text to the console.
     * By doing so, it effectively replays the interactions stored in the YAML file.
     *
     * @param args Command-line arguments, where the first argument specifies the path to the YAML file.
     * @throws IOException If an I/O error occurs while reading the YAML file.
     */
    public static void main(String[] args) throws IOException {
        // Reads the content of a YAML file specified by the first command-line argument (args[0])
        // and feeds it to the SaysOutput object, effectively replaying the interactions stored in the YAML file.
        Wires.replay(args[0], new SaysOutput());
    }
}
