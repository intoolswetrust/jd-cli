package com.github.kwart.jd.sample;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;

/**
 * Builders called in the usual multi-line style.
 */
public final class BuilderChain {

    /**
     * Builds a locale.
     */
    public Locale swissGerman() {
        return new Locale.Builder()
                .setLanguage("de")
                .setRegion("CH")
                .build();
    }

    /**
     * Builds a date.
     */
    public LocalDate swissNationalDay() {
        return LocalDate.now()
                .withMonth(Month.AUGUST.getValue())
                .withDayOfMonth(1);
    }

    /**
     * Returns the root locale (signature and body on the same line, as many projects write their one-liners).
     */
    @SuppressWarnings({ "checkstyle:leftcurly", "checkstyle:rightcurly" })
    public Locale rootLocale() { return Locale.ROOT; }
}
