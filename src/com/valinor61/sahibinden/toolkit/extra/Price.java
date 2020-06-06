package com.valinor61.sahibinden.toolkit.extra;

import com.valinor61.sahibinden.toolkit.Tools;
import org.jetbrains.annotations.NotNull;

public class Price implements Comparable<Price>, NumberString {
    int priceValue;

    public Price(int priceValue) {
        this.priceValue = priceValue;
    }

    public int getPriceValue() {
        return priceValue;
    }

    @Override
    public String toString() {
        return Tools.formatToTl(priceValue);
    }

    @Override
    public int compareTo(@NotNull Price o) {
        return Integer.compare(this.priceValue, o.priceValue);
    }
}