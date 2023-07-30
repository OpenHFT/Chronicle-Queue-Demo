package event.driven.program;

import event.driven.program.api.Says;

/**
 * This is a SaysOutput class.
 * It provides an implementation of the {@link Says} interface that outputs the given words directly to the standard output.
 *
 * @since 2023-07-30
 */
public class SaysOutput implements Says {

    /**
     * This method implements the {@code say} method of the {@link Says} interface.
     * It takes a string of words and prints them directly to the standard output.
     *
     * @param words The words to be printed to the standard output.
     */
    public void say(String words) {
        // Prints the given words directly to the standard output
        System.out.println(words);
    }
}
