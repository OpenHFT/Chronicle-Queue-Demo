package event.driven.program;

import event.driven.program.api.Says;

public class SaysOutput implements Says {
    public void say(String words) {
        System.out.println(words);
    }
}