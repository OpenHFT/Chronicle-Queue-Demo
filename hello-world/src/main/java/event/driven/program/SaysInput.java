/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package event.driven.program;

import event.driven.program.api.Says;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is a SaysInput class.
 * It provides a static method to read text lines from the standard input and forward them to a provided {@link Says} implementation.
 */
public class SaysInput {

    /**
     * This method reads text lines from the standard input and calls the {@code say} method of the provided {@link Says} object for each line.
     * It continues reading and processing lines until the end of the input is reached (i.e., the input returns {@code null}).
     *
     * @param says An implementation of the {@link Says} interface that will handle the lines of text read from the input.
     * @throws IOException If an I/O error occurs while reading from the input.
     */
    public static void input(Says says) throws IOException {
        // Creates a BufferedReader to read lines from the standard input
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        // Continuously reads lines from the standard input until reaching the end
        for (String line; ((line = br.readLine()) != null); )
            // Calls the say method of the provided Says object with the read line
            says.say(line);
    }
}
