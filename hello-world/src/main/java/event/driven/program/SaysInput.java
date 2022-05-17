package event.driven.program;

import event.driven.program.api.Says;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SaysInput {
    public static void input(Says says) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        for (String line; ((line = br.readLine()) != null); )
            says.say(line);
    }
}
