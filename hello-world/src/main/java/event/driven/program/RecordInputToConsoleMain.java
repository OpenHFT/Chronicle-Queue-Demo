package event.driven.program;

import event.driven.program.api.Says;

import java.io.IOException;

public class RecordInputToConsoleMain {
    public static void main(String[] args) throws IOException {
        // Writes text in each call to say(line) to the console
        final Says says = new SaysOutput();
        // Takes each line input and calls say(line) each time
        SaysInput.input(says);
    }
}
