/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import java.util.Collections;
import java.util.Map;

/**
 * Minimal placeholder for the agitator API. The demo suite only needs the factory
 * methods to exist so code compiles; the default implementation returns no variants.
 */
public interface YamlAgitator {

    static YamlAgitator messageMissing() {
        return new MessageMissingAgitator();
    }

    static YamlAgitator messageMissing(int limit) {
        return new MessageMissingAgitator(limit);
    }

    static YamlAgitator duplicateMessage() {
        return new DuplicateMessageAgitator();
    }

    static YamlAgitator duplicateMessage(int limit) {
        return new DuplicateMessageAgitator(limit);
    }

    static YamlAgitator missingFields(String... fields) {
        return new MissingFieldAgitator(fields);
    }

    static YamlAgitator overrideFields(String... fields) {
        return new OverrideFieldAgitator(fields);
    }

    static YamlAgitator replaceAll(String name, String regex, String replaceAll) {
        return new RegexFieldAgitator(name, regex, replaceAll);
    }

    default Map<String, String> generateInputs(String yaml) {
        return Collections.emptyMap();
    }
}
