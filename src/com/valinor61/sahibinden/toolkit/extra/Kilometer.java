package com.valinor61.sahibinden.toolkit.extra;


import java.text.NumberFormat;
import java.util.Locale;

public class Kilometer implements Comparable<Kilometer>, NumberString {
    private final int kmValue;

    public Kilometer(int kmValue) {
        this.kmValue = kmValue;
    }

    @Override
    public String toString() {
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(kmValue);
    }

    @Override
    public int compareTo(Kilometer o) {
        return Integer.compare(this.kmValue, o.kmValue);
    }
}