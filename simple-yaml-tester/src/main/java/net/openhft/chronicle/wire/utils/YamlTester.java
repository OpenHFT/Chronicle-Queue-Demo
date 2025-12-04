/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.io.IORuntimeException;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.ValueOut;
import net.openhft.chronicle.wire.DocumentContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Minimal implementation of the Chronicle Wire {@code YamlTester} API used by the demos.
 * It replays YAML input into the component under test and captures the resulting YAML output
 * so test code can compare {@link #expected()} and {@link #actual()}.
 */
public interface YamlTester {
    boolean REGRESS_TESTS = Jvm.getBoolean("regress.tests");
    boolean BASE_TESTS = Jvm.getBoolean("base.tests");

    @SuppressWarnings("unchecked")
    static YamlTester runTest(Class<?> implClass, String path) {
        for (Constructor<?> cons : implClass.getDeclaredConstructors()) {
            if (cons.getParameterCount() == 1) {
                Class<?> param = cons.getParameterTypes()[0];
                if (param.isInterface()) {
                    return runTest(out -> {
                        try {
                            return cons.newInstance(out);
                        } catch (Exception e) {
                            throw new AssertionError(e);
                        }
                    }, (Class<Object>) param, path);
                }
            }
        }
        throw new IllegalArgumentException("No single-argument interface constructor found for " + implClass.getName());
    }

    static <T> YamlTester runTest(Function<T, Object> builder, Class<T> outClass, String path) {
        try {
            String inText = readResource(path + "/in.yaml", outClass);
            String outText = readResource(path + "/out.yaml", outClass);
            String setupText = readResource(path + "/_setup.yaml", outClass);
            Path expectedPath = Paths.get(path, "out.yaml");
            Function<WireOut, T> outFactory = w -> w.methodWriter(outClass);
            return new SimpleYamlTester<>(builder, outFactory, inText, outText, setupText, expectedPath);
        } catch (IOException ioe) {
            throw new IORuntimeException(ioe);
        }
    }

    static <T> YamlTester runTest(Function<T, Object> builder, Function<WireOut, T> outFunction, String path) {
        try {
            String inText = readResource(path + "/in.yaml", null);
            String outText = readResource(path + "/out.yaml", null);
            String setupText = readResource(path + "/_setup.yaml", null);
            Path expectedPath = Paths.get(path, "out.yaml");
            return new SimpleYamlTester<>(builder, outFunction, inText, outText, setupText, expectedPath);
        } catch (IOException ioe) {
            throw new IORuntimeException(ioe);
        }
    }

    /**
        * Generate altered inputs for agitated tests. The minimal implementation returns an empty map.
        */
    default Map<String, String> agitate(YamlAgitator agitator) {
        return Collections.emptyMap();
    }

    String expected();

    String actual();

    /**
     * Helper to write the current actual output back to disk when {@code -Dregress.tests=true}.
     */
    default void writeExpected(Path destination) {
        if (!REGRESS_TESTS || destination == null)
            return;
        try {
            Files.createDirectories(destination.getParent());
            Files.write(destination, actual().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            Jvm.warn().on(YamlTester.class, "Unable to update " + destination + " " + e.getMessage());
        }
    }

    static String readResource(String location, Class<?> anchor) throws IOException {
        Path path = Paths.get(location);
        if (Files.exists(path))
            return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);

        ClassLoader cl = anchor != null ? anchor.getClassLoader() : Thread.currentThread().getContextClassLoader();
        if (cl == null)
            cl = YamlTester.class.getClassLoader();
        try (InputStream is = cl.getResourceAsStream(location)) {
            if (is != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int read;
                while ((read = is.read(buffer)) >= 0) {
                    bout.write(buffer, 0, read);
                }
                return new String(bout.toByteArray(), StandardCharsets.UTF_8);
            }
        }
        return "";
    }

    static Class<?>[] writerInterfaces(Class<?> primary) {
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        collectInterfaces(primary, interfaces);
        return interfaces.toArray(new Class<?>[0]);
    }

    static void collectInterfaces(Class<?> type, Set<Class<?>> interfaces) {
        if (type == null || type == Object.class)
            return;
        if (type.isInterface())
            interfaces.add(type);
        for (Class<?> iface : type.getInterfaces()) {
            collectInterfaces(iface, interfaces);
        }
        for (java.lang.reflect.Method m : type.getMethods()) {
            Class<?> rt = m.getReturnType();
            if (rt != null && rt.isInterface())
                collectInterfaces(rt, interfaces);
        }
    }
}
