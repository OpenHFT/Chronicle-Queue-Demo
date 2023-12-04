package net.openhft.chronicle.queue.simple.input;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.util.Scanner;

/**
 * Created by catherine on 17/07/2016.
 */
public class InputMain {
    public static void main(String[] args) {
        String path = "queue";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptAppender appender = queue.createAppender();
        Scanner read = new Scanner(System.in);
        while (true) {
            System.out.println("type something");
            String line = read.nextLine();
            if (line.isEmpty())
                break;
            appender.writeText(line);
        }
        System.out.println("... bye.");
    }
}
