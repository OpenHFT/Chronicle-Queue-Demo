package event.driven.program;

import java.io.IOException;

public class DirectMain {
    public static void main(String[] args) throws IOException {
        SaysInput.input(new SaysOutput());
    }
}