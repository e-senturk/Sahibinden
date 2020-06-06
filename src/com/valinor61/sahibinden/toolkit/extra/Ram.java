package com.valinor61.sahibinden.toolkit.extra;

import com.valinor61.sahibinden.toolkit.Tools;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Locale;

public class Ram implements Comparable<Ram>, NumberString {
    private final int ramValue;

    public Ram(String ramValue) {
        this.ramValue = Tools.onlyNumbers(ramValue);
    }

    @Override
    public String toString() {
        String appendix = " GB";
        if (ramValue >= 512) {
            appendix = "MB";
        }
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(ramValue) + appendix;
    }

    @Override
    public int compareTo(@NotNull Ram o) {
        return Integer.compare(this.ramValue, o.ramValue);
    }
}
