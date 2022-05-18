package event.driven.program;

import event.driven.program.api.Says;

public class AddsExclamation implements Says {
    private final Says out;

    public AddsExclamation(Says out) {
        this.out = out;
    }

    public void say(String words) {
        this.out.say(words + "!");
    }
}
