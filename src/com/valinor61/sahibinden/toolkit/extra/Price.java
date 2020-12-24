package com.valinor61.sahibinden.toolkit.extra;

import com.valinor61.sahibinden.toolkit.Tools;

public class Price implements Comparable<Price>, NumberString {
    private final int priceValue;

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
    public int compareTo(Price o) {
        return Integer.compare(this.priceValue, o.priceValue);
    }
}