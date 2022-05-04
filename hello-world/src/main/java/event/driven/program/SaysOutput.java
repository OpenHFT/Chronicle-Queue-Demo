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

public class SaysOutput implements Says {
    public void say(String words) {
        System.out.println(words);
    }
}