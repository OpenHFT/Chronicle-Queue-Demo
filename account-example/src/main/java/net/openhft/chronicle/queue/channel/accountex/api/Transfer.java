/*
 * Copyright 2016-2022 chronicle.software
 *
 *       https://chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.openhft.chronicle.queue.channel.accountex.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

public class Transfer extends SelfDescribingMarshallable {
    @NanoTime
    private long time;
    @Base85
    private long from, to;
    private double amount;

    public long time() {
        return time;
    }

    public Transfer time(long time) {
        this.time = time;
        return this;
    }

    public long from() {
        return from;
    }

    public Transfer from(long from) {
        this.from = from;
        return this;
    }

    public long to() {
        return to;
    }

    public Transfer to(long to) {
        this.to = to;
        return this;
    }

    public double amount() {
        return amount;
    }

    public Transfer amount(double amount) {
        this.amount = amount;
        return this;
    }
}
