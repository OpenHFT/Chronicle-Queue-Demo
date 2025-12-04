/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import java.util.Collections;
import java.util.Map;

final class RegexFieldAgitator implements YamlAgitator {
    private final String name;
    private final String regex;
    private final String replaceAll;

    RegexFieldAgitator(String name, String regex, String replaceAll) {
        this.name = name;
        this.regex = regex;
        this.replaceAll = replaceAll;
    }

    @Override
    public Map<String, String> generateInputs(String yaml) {
        return Collections.emptyMap();
    }
}
