package event.driven.program;

import java.io.IOException;

/**
 * This is a DirectWithExclamationMain class.
 * It reads lines of text from the standard input, adds an exclamation mark to the end of each line, and then prints the result to the console.
 * This class leverages the {@link AddsExclamation} and {@link SaysOutput} implementations to achieve this functionality.
 */
public class DirectWithExclamationMain {
    /**
     * This method creates a new {@link AddsExclamation} object that wraps a {@link SaysOutput} object.
     * It will add an exclamation mark to the end of each input message and then print the result to the console.
     * This behavior is orchestrated by the {@link SaysInput} class, which reads the input and forwards it to the given {@link Says} implementation.
     *
     * @param args Command-line arguments, not used in this implementation.
     * @throws IOException If an I/O error occurs while reading from the standard input.
     */
    public static void main(String[] args) throws IOException {
        // Create a new AddsExclamation object that wraps a SaysOutput object.
        // This will add an exclamation mark to the end of each input message,
        // and then print the result to the console.
        SaysInput.input(new AddsExclamation(new SaysOutput()));
    }
}
