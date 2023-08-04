package net.openhft.chronicle.queue.simple.translator;

/**
 * A simple implementation of the MessageConsumer interface.
 * This class is responsible for translating certain English words to their French counterparts.
 *
 * Created by catherine on 26/07/2016.
 */
public class SimpleTranslator implements MessageConsumer {

    // MessageConsumer instance to which the translated message is forwarded
    private MessageConsumer out;

    /**
     * Constructs a SimpleTranslator with a specified MessageConsumer.
     *
     * @param out the MessageConsumer to which the translated message is sent
     */
    public SimpleTranslator(MessageConsumer out) {
        this.out = out;
    }

    /**
     * Translates the given message by replacing certain English words with their French equivalents,
     * and sends the translated message to the associated MessageConsumer.
     *
     * @param text the message to translate
     */
    @Override
    public void onMessage(String text) {
        System.out.println("translating " + text);

        // Translate some English words to French
        text = text.replaceAll("hello", "salut");
        text = text.replaceAll("bye", "salut");
        text = text.replaceAll("apple", "pomme");
        text = text.replaceAll("banana", "banane");
        text = text.replaceAll("nice", "sympa");
        text = text.replaceAll("goodnight", "bonne nuit");
        text = text.replaceAll("happy", "heureux");
        text = text.replaceAll("green", "vert");
        text = text.replaceAll("house", "maison");
        text = text.replaceAll("hat", "chapeau");
        text = text.replaceAll("crazy", "fou");
        text = text.replaceAll("song", "chanson");
        text = text.replaceAll("day", "jour");
        text = text.replaceAll("night", "nuit");
        text = text.replaceAll("food", "nourriture");
        text = text.replaceAll("drink", "boisson");
        text = text.replaceAll("run", "courir");
        text = text.replaceAll("walk", "marcher");
        text = text.replaceAll("love", "amour");
        text = text.replaceAll("hate", "haine");

        System.out.println("... to: " + text);

        // Pass the translated text to the output consumer
        out.onMessage(text);
    }
}
