/*
 * Copyright 2021 - http://chronicle.software
 *
 * Chronicle software holds the rights to this software and it may not be redistributed to another organisation or a different team within your organisation.
 *
 * You may only use this software if you have prior written consent from Chronicle Software.
 *
 * This written consent may take the form of a valid (non expired) software licence.
 */
package event.driven.program;

import event.driven.program.api.Says;
import net.openhft.chronicle.wire.Wires;

import java.io.IOException;

public class RecordInputToConsoleMain {
    public static void main(String[] args) throws IOException {
        // writes text in each calls to say(line) to the console
        final Says says = new SaysOutput();
        // Takes each line input and calls say(line) each time
        SaysInput.input(says);
    }
}
