package net.openhft.chronicle.queue.simple.translator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Translates messages from English to French by replacing predefined English words
 * with their French equivalents. The translated messages are then forwarded to a
 * specified {@link MessageConsumer}.
 *
 * <p>
 * This implementation uses a case-insensitive approach and ensures that only whole
 * words are replaced to prevent partial matches within larger words.
 * </p>
 *
 * <p>
 * Example:
 * </p>
 * <pre>
 *     SimpleTranslator translator = new SimpleTranslator(outConsumer);
 *     translator.onMessage("Hello apple");
 *     // Translated message: "salut pomme"
 * </pre>
 *
 * @since 1.0
 */
public class SimpleTranslator implements MessageConsumer {

    private static final Logger LOGGER = Logger.getLogger(SimpleTranslator.class.getName());

    // Immutable map containing English to French translations
    private static final Map<String, String> TRANSLATIONS;

    // Precompiled regex pattern for efficient matching
    private static final Pattern WORD_BOUNDARY_PATTERN;

    static {
        Map<String, String> translations = new HashMap<>();
        translations.put("animal", "animal");
        translations.put("apple", "pomme");
        translations.put("baby", "bébé");
        translations.put("banana", "banane");
        translations.put("bird", "oiseau");
        translations.put("book", "livre");
        translations.put("bye", "salut");
        translations.put("car", "voiture");
        translations.put("child", "enfant");
        translations.put("coffee", "café");
        translations.put("crazy", "fou");
        translations.put("city", "ville");
        translations.put("country", "pays");
        translations.put("day", "jour");
        translations.put("drink", "boisson");
        translations.put("family", "famille");
        translations.put("flower", "fleur");
        translations.put("food", "nourriture");
        translations.put("game", "jeu");
        translations.put("goodnight", "bonne nuit");
        translations.put("green", "vert");
        translations.put("hat", "chapeau");
        translations.put("happy", "heureux");
        translations.put("hate", "haine");
        translations.put("hello", "salut");
        translations.put("house", "maison");
        translations.put("love", "amour");
        translations.put("moon", "lune");
        translations.put("music", "musique");
        translations.put("movie", "film");
        translations.put("nice", "sympa");
        translations.put("night", "nuit");
        translations.put("ocean", "océan");
        translations.put("river", "rivière");
        translations.put("road", "route");
        translations.put("run", "courir");
        translations.put("star", "étoile");
        translations.put("sun", "soleil");
        translations.put("tea", "thé");
        translations.put("tree", "arbre");
        translations.put("travel", "voyage");
        translations.put("walk", "marcher");
        translations.put("work", "travail");

        TRANSLATIONS = Collections.unmodifiableMap(translations);

        // Build a regex pattern that matches any of the English words with word boundaries
        String patternString = "\\b(" + String.join("|", TRANSLATIONS.keySet()) + ")\\b";
        WORD_BOUNDARY_PATTERN = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
    }

    // MessageConsumer instance to which the translated message is forwarded
    private final MessageConsumer out;

    /**
     * Constructs a {@code SimpleTranslator} with the specified {@link MessageConsumer}.
     *
     * @param out the {@code MessageConsumer} to which the translated message is sent; must not be {@code null}
     * @throws NullPointerException if {@code out} is {@code null}
     */
    public SimpleTranslator(MessageConsumer out) {
        this.out = Objects.requireNonNull(out, "Output MessageConsumer cannot be null");
    }

    /**
     * Translates the given message by replacing predefined English words with their French equivalents,
     * and sends the translated message to the associated {@link MessageConsumer}.
     *
     * <p>
     * The translation is case-insensitive and only whole words are replaced to prevent partial matches
     * within larger words.
     * </p>
     *
     * @param text the message to translate; must not be {@code null}
     * @throws IllegalArgumentException if {@code text} is {@code null}
     */
    @Override
    public void onMessage(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Input text cannot be null");
        }

        LOGGER.log(Level.FINE, "Translating message: \"{0}\"", sanitizeForLog(text));

        Matcher matcher = WORD_BOUNDARY_PATTERN.matcher(text);
        StringBuffer translatedBuffer = new StringBuffer();

        while (matcher.find()) {
            String matchedWordOriginal = matcher.group(1);
            String matchedWord = matchedWordOriginal.toLowerCase(Locale.ROOT);
            String replacement = TRANSLATIONS.get(matchedWord);
            if (replacement != null) {
                matcher.appendReplacement(translatedBuffer, Matcher.quoteReplacement(replacement));
                LOGGER.log(Level.FINER, "Replaced \"{0}\" with \"{1}\"",
                        new Object[]{sanitizeForLog(matchedWordOriginal), sanitizeForLog(replacement)});
            }
        }
        matcher.appendTail(translatedBuffer);

        String translatedText = translatedBuffer.toString();
        LOGGER.log(Level.FINE, "Translated message: \"{0}\"", sanitizeForLog(translatedText));

        // Forward the translated text to the output consumer
        out.onMessage(translatedText);
    }

    /**
     * Adds a new translation to the translator. This method is not supported in the current implementation
     * as the translations map is immutable. To add new translations, modify the TRANSLATIONS map in the source code.
     *
     * @param english the English word to translate
     * @param french the French translation
     * @throws UnsupportedOperationException always, as translations cannot be modified at runtime
     */
    public void addTranslation(String english, String french) {
        throw new UnsupportedOperationException("Dynamic translation addition is not supported");
    }

    private static String sanitizeForLog(String value) {
        if (value == null) {
            return "";
        }
        return value.replace('\r', '_').replace('\n', '_');
    }
}
