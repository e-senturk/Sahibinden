package com.valinor61.sahibinden.toolkit.extra;

import org.jetbrains.annotations.NotNull;

public class Difference implements Comparable<Difference>, NumberString {
    private final int diferenceValue;
    private final int averageValue;
    private final int differenceRatio;

    public Difference(int diferenceValue, int averageValue) {
        this.diferenceValue = diferenceValue;
        this.averageValue = averageValue;
        if (averageValue == 0) {
            this.differenceRatio = 0;
        } else {
            this.differenceRatio = diferenceValue * 100 / averageValue;
        }
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
            return "% 00";
        }
    }

    @Override
    public int compareTo(@NotNull Difference o) {
        return Integer.compare(this.differenceRatio, o.differenceRatio);
    }
}