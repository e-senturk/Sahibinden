package com.valinor61.sahibinden.toolkit;

import com.valinor61.sahibinden.data.DataBase;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Tools {


    //Tools sadece araçları içerir tools üzerinden nesne oluşturulamaz.
    private Tools() {
    }

    //İki kelime arasındakileri bulup bir liste oluşturan fonksiyon
    @NotNull
    public static LinkedList<String> findBetweenList(String split, String begin, String end) {
        LinkedList<String> newList = new LinkedList<>();
        int index = 0;
        boolean find = true;
        while (find && index < split.length()) {
            int start = split.indexOf(begin, index);
            int stop = split.indexOf(end, start + begin.length());
            if (start == -1 || stop == -1) {
                find = false;
            } else {
                index = stop + end.length();
                newList.add(split.substring(start + begin.length(), stop).trim());
            }

        }
        return newList;
    }

    //İki kelime arasındaki ilk bulunan bölümü bulan fonksiyon.
    @NotNull
    public static String findBetween(@NotNull String split, String begin, String end, int startIndex) {
        int start = split.indexOf(begin, startIndex);
        int stop = split.indexOf(end, start + begin.length());
        if (start == -1 || stop == -1) {
            return "";
        } else {
            return split.substring(start + begin.length(), stop).trim();
        }
    }

    @NotNull
    public static String removeNbsp(@NotNull String text) {
        if (!text.contains("&nbsp")) {
            return text;
        }
        text = "<<<" + text;
        return findBetween(text, "<<<", "&", 0);
    }

    @NotNull
    public static String removeTurkishCharacter(@NotNull String text) {
        return text.replace("ç", "c").replace("ö", "o").replace("ş", "s")
                .replace("ğ", "g").replace("ü", "u").replace("ı", "i")
                .replace("Ç", "C").replace("Ö", "O").replace("Ş", "S")
                .replace("Ğ", "G").replace("Ü", "U").replace("İ", "i").replace("ë", "e").replace("é", "e");
    }

    @NotNull
    public static String formatText(String text) {
        return removeTurkishCharacter(removeNbsp(text.toLowerCase())).trim().replace("&amp;", "").replace(" ", "-")
                .replace("---", "-").replace("--", "-");
    }


    public static String modeProcessor(String processorName, boolean[] value) {
        StringBuilder processor = new StringBuilder();
        String brand = "", series = "", model = "";
        if (value[0] && value[1] && value[2]) {
            return processorName;
        } else {
            String[] x = processorName.split("-");
            if (x.length >= 4) {
                model = x[3];
                if (x.length >= 5) {
                    model += "-" + x[4];
                }
                if (x.length >= 6) {
                    model += "-" + x[5];
                }
                if (x.length >= 7) {
                    model += "-" + x[6];
                }
                if (x.length >= 8) {
                    model += "-" + x[7];
                }
            }
            if (x.length >= 3) {
                series = x[1] + "-" + x[2];
            }
            if (x.length >= 1) {
                brand = x[0];
            }
            if (value[0]) {
                processor.append(brand).append("-");
            }
            if (value[1]) {
                processor.append(series).append("-");
            }
            if (value[2]) {
                processor.append(model).append("-");
            }
            if (processor.length() > 0) {
                processor.setLength(processor.length() - 1);
            }
            return processor.toString();
        }
    }


    public static String findMatch(DataBase data, String titleFind, String textFind, boolean enabled, boolean identical) {
        if (!enabled) {
            return "";
        }
        if (data.getType().equals(titleFind)) {
            if (identical) {
                if (data.getName().equals(textFind)) {
                    return data.getLinkCode() + "&";
                }
            } else {
                if (data.getName().contains(textFind)) {
                    return data.getLinkCode() + "&";
                }
            }
        }
        return "";
    }

    public static int onlyNumbers(String numberText) {
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < numberText.length(); i++) {
            if (numberText.charAt(i) <= '9' && numberText.charAt(i) >= '0') {
                number.append(numberText.charAt(i));
            }
        }
        if (number.length() == 0) {
            return 0;
        }
        return Integer.parseInt(number.toString());
    }

    public static String formatToTl(int average) {
        return NumberFormat.getNumberInstance(Locale.GERMAN).format(average) + " TL";
    }

    public static String formatLastXEN(String text) {
        switch (text) {
            case "Son 1 gün":
                return "1day";
            case "Son 3 gün":
                return "3days";
            case "Son 7 gün":
                return "7days";
            case "Son 15 gün":
                return "15days";
            case "Son 30 gün":
                return "30days";
            default:
                return "";
        }
    }

    public static String formatLastXTR(String text) {
        switch (text) {
            case "1day":
                return "Son 1 gün";
            case "3days":
                return "Son 3 gün";
            case "7days":
                return "Son 7 gün";
            case "15days":
                return "Son 15 gün";
            case "30days":
                return "Son 30 gün";
            default:
                return "";
        }
    }

    public static String getInfo(String html) {
        return findBetween(html, "<div class=\"classifiedBreadCrumb\">", "</div>", 0);
    }

    public static String formatHorsePower(String text) {
        ArrayList<Integer> nums = findNumbers(text);
        if (nums.size() > 1) {
            return formatText(text);
        }
        if (nums.size() == 1) {
            if (nums.get(0) < 50) {
                return "50-hp'ye-kadar";
            } else if (nums.get(0) > 600) {
                return "601-hp-ve-uzeri";
            } else {
                int k = nums.get(0) / 25;
                return (k * 25 + 1) + "-" + (k + 1) * 25 + "-hp";
            }
        } else return text;
    }

    public static String formatEngine(String text) {
        ArrayList<Integer> nums = findNumbers(text);
        if (nums.size() > 2) {
            return formatText(text);
        }
        if (nums.size() == 2 || nums.size() == 1) {
            if (nums.get(0) < 1300) {
                return "1300-cm3'-e-kadar";
            } else if (nums.get(0) < 1600) {
                return "1301-1600-cm3";
            } else if (nums.get(0) < 2000) {
                int k = nums.get(0) / 200;
                return (k * 200 + 1) + "-" + (k + 1) * 200 + "-cm3";
            } else if (nums.get(0) > 6000) {
                return "6001-cm3-ve-uzeri";
            } else if (nums.get(0) > 2000) {
                int k = nums.get(0) / 500;
                return (k * 500 + 1) + "-" + (k + 1) * 500 + "-cm3";
            } else return text;
        } else return text;
    }

    public static ArrayList<Integer> findNumbers(String text) {
        ArrayList<Integer> numbers = new ArrayList<>(5);
        String[] list = text.split(" ");
        for (String x : list) {
            if (Tools.onlyNumbers(x) != 0) {
                numbers.add(Tools.onlyNumbers(x));
            }
        }
        return numbers;
    }

}
