package run.chronicle.queue.channel;/*
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

import net.openhft.chronicle.wire.TextMethodTester;
import org.junit.Test;
import run.chronicle.queue.channel.api.AccountManagerOut;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AccountManagerTest {

    static void testMessages(String path) throws IOException {
        TextMethodTester test = new TextMethodTester<>(
                path + "/in.yaml",
                AccountManager::new,
                AccountManagerOut.class,
                path + "/out.yaml")
                .setup(path + "/setup.yaml")
                .run();
        assertEquals(test.expected(), test.actual());
    }

    @Test
    public void selfTransfer() throws IOException {
        testMessages("account/self");
    }

}