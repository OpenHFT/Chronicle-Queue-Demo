package event.driven.program.api;

/**
 * This is a Says interface.
 * It defines a contract for classes that need to handle speaking or outputting words.
 * Implementations of this interface are responsible for determining how the words are said or output.
 *
 * @since 2023-07-30
 */
public interface Says {

    /**
     * This method is expected to be implemented to say or output the given words.
     * The specific behavior of how the words are said or output depends on the implementing class.
     *
     * @param words The text to be said or output.
     */
    void say(String words);
}