/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.wire.utils;

import net.openhft.chronicle.core.io.IORuntimeException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Simplified builder that assembles {@link YamlTester} instances for a set of YAML paths.
 * Only the pieces required by the demo tests are implemented; the remaining setters are
 * retained for compatibility and return {@code this}.
 */
public class YamlTesterParametersBuilder<T> {
    private final Function<T, Object> builder;
    private final Class<T> outClass;
    private final List<String> paths;
    private final Set<Class<?>> additionalOutputClasses = new LinkedHashSet<>();

    private YamlAgitator[] agitators = {};
    private Function<T, net.openhft.chronicle.core.onoes.ExceptionHandler> exceptionHandlerFunction;
    private boolean exceptionHandlerFunctionAndLog;
    private Predicate<String> testFilter = s -> true;
    private Function<String, String> inputFunction;

    public YamlTesterParametersBuilder(Function<T, Object> builder, Class<T> outClass, String paths) {
        this(builder, outClass, Arrays.asList(paths.split(" *, *")));
    }

    public YamlTesterParametersBuilder(Function<T, Object> builder, Class<T> outClass, List<String> paths) {
        this.builder = builder;
        this.outClass = outClass;
        this.paths = paths;
    }

    public YamlTesterParametersBuilder<T> agitators(YamlAgitator... agitators) {
        this.agitators = agitators;
        return this;
    }

    public YamlTesterParametersBuilder<T> exceptionHandlerFunction(Function<T, net.openhft.chronicle.core.onoes.ExceptionHandler> exceptionHandlerFunction) {
        this.exceptionHandlerFunction = exceptionHandlerFunction;
        return this;
    }

    public YamlTesterParametersBuilder<T> exceptionHandlerFunctionAndLog(boolean exceptionHandlerFunctionAndLog) {
        this.exceptionHandlerFunctionAndLog = exceptionHandlerFunctionAndLog;
        return this;
    }

    public YamlTesterParametersBuilder<T> testFilter(Predicate<String> testFilter) {
        this.testFilter = testFilter;
        return this;
    }

    public YamlTesterParametersBuilder<T> inputFunction(Function<String, String> inputFunction) {
        this.inputFunction = inputFunction;
        return this;
    }

    public YamlTesterParametersBuilder<T> addOutputClass(Class<?> outputClass) {
        additionalOutputClasses.add(outputClass);
        return this;
    }

    public Predicate<String> testFilter() {
        return testFilter;
    }

    /**
     * Builds a parameter list suitable for JUnit {@code @Parameters}.
     * Only base tests are produced; agitator and combination generation are intentionally minimal.
     */
    public List<Object[]> get() {
        List<Object[]> params = new ArrayList<>();
        for (String rawPath : paths) {
            String path = rawPath.trim();
            if (path.isEmpty())
                continue;
            if (!testFilter.test(path))
                continue;
            Path outPath = Paths.get(path, "out.yaml");
            String inText = readQuietly(path + "/in.yaml");
            String outText = readQuietly(path + "/out.yaml");
            String setupText = readQuietly(path + "/_setup.yaml");

            if (inputFunction != null) {
                inText = inputFunction.apply(inText);
                setupText = inputFunction.apply(setupText);
            }

            YamlTester tester = new SimpleYamlTester<>(builder, outClass, inText, outText, setupText, outPath);
            params.add(new Object[]{path, tester});
        }
        return params;
    }

    private String readQuietly(String location) {
        try {
            return YamlTester.readResource(location, outClass);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}
