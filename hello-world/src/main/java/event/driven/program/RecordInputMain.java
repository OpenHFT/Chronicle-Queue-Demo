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

import java.io.IOException;
import net.openhft.chronicle.wire.Wires;

public class RecordInputMain {
    public static void main(String[] args) throws IOException {
        final Says says = Wires.recordAsYaml(Says.class, System.out);
        SaysInput.input(says);
    }
}
