package event.driven.program;

import java.io.IOException;

public class DirectWithExclamationMain {
    public static void main(String[] args) throws IOException {
        // Adds exclamation mark to input messages
        SaysInput.input(new AddsExclamation(new SaysOutput()));
    }
}
