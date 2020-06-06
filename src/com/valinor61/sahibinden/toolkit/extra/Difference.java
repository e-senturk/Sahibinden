package com.valinor61.sahibinden.toolkit.extra;

import org.jetbrains.annotations.NotNull;

public class Difference implements Comparable<Difference>, NumberString {
    private final int diferenceValue;
    private final int averageValue;

    public Difference(int diferenceValue, int averageValue) {
        this.diferenceValue = diferenceValue;
        this.averageValue = averageValue;
    }

    @Override
    public String toString() {
        try {
            int x = diferenceValue * 100 / averageValue;
            String t = "";
            if (x < 10 && x >= 0) {
                t = " 0";
            } else if (x > -10 && x < 0) {
                x = x * -1;
                t = "-0";
            } else if (x >= 10 && x <= 99) {
                t = " ";
            }
            return "% " + t + x;
        } catch (ArithmeticException e) {
            System.out.println("Sıfıra bölme");
            return "% 0";
        }
    }

    @Override
    public int compareTo(@NotNull Difference o) {
        return Integer.compare(this.diferenceValue, o.diferenceValue);
    }
}