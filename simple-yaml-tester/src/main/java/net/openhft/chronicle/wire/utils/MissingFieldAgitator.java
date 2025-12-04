/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import java.util.Collections;
import java.util.Map;

final class MissingFieldAgitator implements YamlAgitator {
    private final String[] fields;

    MissingFieldAgitator(String... fields) {
        this.fields = fields;
    }

    @Override
    public Map<String, String> generateInputs(String yaml) {
        return Collections.emptyMap();
    }
}
