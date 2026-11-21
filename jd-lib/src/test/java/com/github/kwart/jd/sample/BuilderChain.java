package com.github.kwart.jd.sample;

import java.util.Locale;

/**
 * Builders called in the usual multi-line style.
 */
public final class BuilderChain {

    private BuilderChain() {
    }

    /**
     * Builds a greeting.
     */
    public static String greeting(String name) {
        return new StringBuilder()
                .append("Hello, ")
                .append(name)
                .append('!')
                .toString();
    }

    /**
     * Builds a greeting (the old way).
     */
    public static String legacyGreeting(String name) {
        return new StringBuffer()
                .append("Hello, ")
                .append(name)
                .append('!')
                .toString();
    }

    /**
     * Builds a locale.
     */
    public static Locale swissGerman() {
        return new Locale.Builder()
                .setLanguage("de")
                .setRegion("CH")
                .build();
    }
}
