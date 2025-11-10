//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.input;

import net.openhft.chronicle.queue.main.DumpMain;

import java.io.FileNotFoundException;

/**
 * Created by catherine on 18/07/2016.
 */
public class PrintQueueMain {
    public static void main(String[] args) throws FileNotFoundException {
        // Allow the queue path to be specified via command-line arguments
        String path = (args.length > 0) ? args[0] : "queue";

        DumpMain.dump(path);
    }
}
