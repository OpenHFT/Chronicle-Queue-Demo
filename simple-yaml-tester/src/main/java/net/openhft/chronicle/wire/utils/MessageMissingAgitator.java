/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import java.util.Collections;
import java.util.Map;

final class MessageMissingAgitator implements YamlAgitator {
    private final int limit;

    MessageMissingAgitator() {
        this(4);
    }

    MessageMissingAgitator(int limit) {
        this.limit = limit;
    }

    @Override
    public Map<String, String> generateInputs(String yaml) {
        // Minimal implementation: no agitated variants are produced.
        return Collections.emptyMap();
    }
}
