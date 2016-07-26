package net.openhft.chronicle.queue.simple.translator;

/**
 * Created by catherine on 26/07/2016.
 */
public class SimpleTranslator implements MessageConsumer {
    private MessageConsumer out;

    public SimpleTranslator(MessageConsumer out) {
        this.out = out;
    }

    @Override
    public void onMessage(String text) {
        System.out.println("translating " + text);
        text = text.replaceAll("hello", "salut");
        text = text.replaceAll("apple", "pomme");
        text = text.replaceAll("nice", "sympa");
        text = text.replaceAll("goodnight", "bonne nuit");
        text = text.replaceAll("happy", "heureux");
        text = text.replaceAll("green", "vert");
        text = text.replaceAll("house", "maison");
        text = text.replaceAll("hat", "chapeau");
        text = text.replaceAll("crazy", "fou");
        text = text.replaceAll("song", "chanson");
        System.out.println("... to: " + text);
        out.onMessage(text);
    }
}
