package event.driven.program;

import event.driven.program.api.Says;

/**
 * This is an AddsExclamation class implementing the {@link Says} interface.
 * It decorates a given {@link Says} object, adding an exclamation mark to the end of each input message before delegating the call.
 *
 * @since 2023-07-30
 */
public class AddsExclamation implements Says {
    // The Says object to which the decorated calls will be delegated.
    private final Says out;

    /**
     * Constructs a new AddsExclamation object that decorates the given Says object.
     *
     * @param out the original Says object to be decorated
     */
    public AddsExclamation(Says out) {
        this.out = out;
    }

    /**
     * Says the given words with an added exclamation mark at the end.
     * It adds the exclamation mark to the words and delegates the call to the original Says object.
     *
     * @param words The text to be spoken, with an added exclamation mark.
     */
    public void say(String words) {
        // Adds an exclamation mark before delegating the call
        this.out.say(words + "!");
    }
}
