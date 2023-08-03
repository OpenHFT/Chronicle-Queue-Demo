package event.driven.program;

import event.driven.program.api.Says;

import java.io.IOException;

/**
 * This is a RecordInputToConsoleMain class.
 * It reads lines of text from the standard input and forwards them to the {@link SaysOutput} class, which prints them to the console.
 */
public class RecordInputToConsoleMain {
    /**
     * This method creates a new {@link SaysOutput} object that writes text to the console in each call to the {@code say} method.
     * It utilizes the {@link SaysInput} class to take each line of input from the user and calls the {@code say} method on the {@link Says} object each time.
     * In this instance, the {@link Says} object is configured to write directly to the console.
     *
     * @param args Command-line arguments, not used in this implementation.
     * @throws IOException If an I/O error occurs while reading from the standard input.
     */
    public static void main(String[] args) throws IOException {
        // Creates a new SaysOutput object that writes text to the console
        // in each call to the say(line) method.
        final Says says = new SaysOutput();

        // Utilizes the SaysInput class to take each line of input from the user
        // and calls the say(line) method on the Says object each time.
        // In this instance, the Says object is configured to write directly to the console.
        SaysInput.input(says);
    }
}
