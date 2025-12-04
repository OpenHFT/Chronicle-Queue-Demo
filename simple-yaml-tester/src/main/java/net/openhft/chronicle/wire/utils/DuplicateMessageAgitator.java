/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import java.util.Collections;
import java.util.Map;

final class DuplicateMessageAgitator implements YamlAgitator {
    private final int limit;

    DuplicateMessageAgitator() {
        this(4);
    }

    DuplicateMessageAgitator(int limit) {
        this.limit = limit;
    }

    @Override
    public Map<String, String> generateInputs(String yaml) {
        return Collections.emptyMap();
    }
}
