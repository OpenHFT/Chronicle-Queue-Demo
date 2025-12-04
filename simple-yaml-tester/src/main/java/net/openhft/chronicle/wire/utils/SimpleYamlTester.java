/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import net.openhft.chronicle.bytes.Invocation;
import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.wire.WireOut;

import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

final class SimpleYamlTester<T> implements YamlTester {
    private final String expected;
    private final SimpleYamlTesterRunner<T> runner;
    private final Path expectedPath;
    private String actual;

    SimpleYamlTester(Function<T, Object> builder,
                     Class<T> outClass,
                     String input,
                     String expected,
                     String setup,
                     Path expectedPath) {
        this(builder, outWire -> outWire.methodWriter(outClass), input, expected, setup, expectedPath);
    }

    SimpleYamlTester(Function<T, Object> builder,
                     Function<WireOut, T> outFactory,
                     String input,
                     String expected,
                     String setup,
                     Path expectedPath) {
        Objects.requireNonNull(builder);
        Objects.requireNonNull(outFactory);
        this.runner = new SimpleYamlTesterRunner<>(builder, outFactory, input, setup);
        this.expectedPath = expectedPath;
        this.expected = tidy(expected);
    }

    @Override
    public String expected() {
        return expected;
    }

    @Override
    public String actual() {
        if (actual == null) {
            actual = runner.run();
            writeExpected(expectedPath);
        }
        return actual;
    }

    private static String tidy(String text) {
        if (text == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String line : text.replace("\r\n", "\n").split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("#") || trimmed.isEmpty())
                continue;
            sb.append(line).append('\n');
        }
        return sb.toString().trim();
    }
}

final class SimpleYamlTesterRunner<T> {
    private final Function<T, Object> builder;
    private final Function<WireOut, T> outFactory;
    private final String input;
    private final String setup;

    SimpleYamlTesterRunner(Function<T, Object> builder,
                           Function<WireOut, T> outFactory,
                           String input,
                           String setup) {
        this.builder = builder;
        this.outFactory = outFactory;
        this.input = input == null ? "" : input;
        this.setup = setup == null ? "" : setup;
    }

    String run() {
        net.openhft.chronicle.bytes.Bytes<?> outBytes = net.openhft.chronicle.bytes.Bytes.elasticByteBuffer();
        try {
            net.openhft.chronicle.wire.Wire outWire = net.openhft.chronicle.wire.WireType.YAML_ONLY.apply(outBytes);
            T out = outFactory.apply(outWire);
            Object component = builder.apply(out);

            replay(setup, component, outBytes);
            outBytes.clear();
            replay(input, component, outBytes);

            String text = outBytes.toString();
            return tidy(text);
        } finally {
            outBytes.releaseLast();
        }
    }

    private void replay(String yaml, Object target, net.openhft.chronicle.bytes.Bytes<?> outBytes) {
        if (yaml == null || yaml.trim().isEmpty())
            return;
        net.openhft.chronicle.bytes.Bytes<?> inBytes = net.openhft.chronicle.bytes.Bytes.elasticByteBuffer();
        try {
            inBytes.write(yaml);
            net.openhft.chronicle.wire.Wire inWire = net.openhft.chronicle.wire.WireType.YAML_ONLY.apply(inBytes);
            MethodReader reader = inWire.methodReaderBuilder()
                    .methodReaderInterceptorReturns((method, o, args, invocation) -> {
                        outBytes.append("---\n");
                        return invocation.invoke(method, o, args);
                    })
                    .warnMissing(true)
                    .build(target);
            while (reader.readOne()) {
                Jvm.nanoPause();
            }
        } finally {
            inBytes.releaseLast();
        }
    }

    private static String tidy(String text) {
        if (text == null)
            return "";
        String normalised = text.replace("\r\n", "\n");
        boolean startsWithDoc = normalised.startsWith("---");
        String[] lines = normalised.split("\n");
        StringBuilder sb = new StringBuilder();
        if (!startsWithDoc)
            sb.append("---\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String trimmed = line.trim();
            if (trimmed.startsWith("#") || trimmed.isEmpty())
                continue;
            String cleaned = line.replaceAll("![-_.A-Za-z0-9$]+", "");
            cleaned = cleaned.replace(":  ", ": ").replace("  {", " {");
            sb.append(cleaned).append('\n');
            if ("...".equals(trimmed) && i < lines.length - 1) {
                // ensure each document boundary is followed by a header for the next doc
                int j = i + 1;
                while (j < lines.length && lines[j].trim().isEmpty())
                    j++;
                if (j < lines.length && !lines[j].trim().startsWith("---"))
                    sb.append("---\n");
            }
        }
        return sb.toString().trim();
    }
}
