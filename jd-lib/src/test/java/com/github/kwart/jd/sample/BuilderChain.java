package com.github.kwart.jd.sample;

import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import java.util.Objects;

/**
 * Builders called in the usual multi-line style, and one-liners with the signature and the body on the same line.
 */
@SuppressWarnings({ "checkstyle:leftcurly", "checkstyle:rightcurly" })
public final class BuilderChain {

    private String name;

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

    public String getName() { return name; }

    public void setName(String value) { this.name = value; }

    @Override
    public int hashCode() { return Objects.hash(name); }

    @Override
    public boolean equals(Object o) { return o instanceof BuilderChain && Objects.equals(name, ((BuilderChain) o).name); }

    @Override
    public String toString() { return "BuilderChain[" + name + "]"; }
}
