package com.valinor61.sahibinden.toolkit;

import com.valinor61.sahibinden.car.Car;
import com.valinor61.sahibinden.car.Laptop;
import com.valinor61.sahibinden.data.DataBase;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public class Tools {
    private static final String plateLookName = "Plaka / Uyruk";
    private static final String horsePowerLookName = "Motor Gücü";
    private static final String warrantyLookName = "Garanti";
    private static final String engineLookName = "Motor Hacmi";
    private static final String powerLookName = "Çekiş";
    private static final String colorLookName = "Renk";
    private static final String chasisLookName = "Kasa Tipi";
    private static final String processorLaptopLookName = "İşlemci";
    private static final String ramLaptopLookName = "RAM";
    private static final String hddLaptopLookName = "Sabit Disk (HDD)";
    private static final String ssdLaptopLookName = "Sabit Disk (SSD)";
    private static final String gpuLaptopLookName = "Ekran Kartı";
    private static final String screenSizeLaptopLookName = "Ekran Boyutu";
    private static final String resolutionLaptopLookName = "Çözünürlük";
    private static final String sellerLaptopLookName = "Kimden";

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


    public static String calculateCarLink(Car car, LinkedList<DataBase> dataBases, ArrayList<Boolean> activationList, String keyWord, String lastXDays, int[] ranges) {

        StringBuilder link = new StringBuilder();
        link.append("https://www.sahibinden.com/");
        //Marka Bölümü
        link.append(car.getBrand());
        //Seri ve Model Bölümü
        if (activationList.get(0)) {
            link.append("-").append(car.getSeries());
            if (activationList.get(1)) {
                link.append("-").append(car.getModel());
            }
        }
        //Yakıt Bölümü Ekleme
        if (activationList.get(2)) {
            link.append("/").append(car.getFuel());
        }
        //Vites Bölümü
        if (activationList.get(3)) {
            link.append("/").append(car.getGear());
        }
        //Araç Durumu Bölümü
        if (activationList.get(4)) {
            link.append("/").append(car.getStatus());
        }
        //Kimden bölümü
        if (activationList.get(5)) {
            link.append("/").append(car.getSeller());
        }
        //İlk Parçanın Sonu
        link.append("?");
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        StringBuilder third = new StringBuilder();
        for (DataBase x : dataBases) {
            first.append(findMatch(x, plateLookName, car.getPlate(), activationList.get(9), false));
            first.append(findMatch(x, horsePowerLookName, car.getHorsePower(), activationList.get(30), false));
            first.append(findMatch(x, warrantyLookName, car.getWarranty(), activationList.get(10), false));
            second.append(findMatch(x, engineLookName, car.getEngine(), activationList.get(29), false));
            second.append(findMatch(x, powerLookName, car.getPower(), activationList.get(11), false));
            third.append(findMatch(x, colorLookName, car.getColor(), activationList.get(12), false));
            third.append(findMatch(x, chasisLookName, car.getChasis(), activationList.get(13), false));
        }
        if (activationList.get(20)) {
            link.append("pagingSize=50").append("&");
        }
        //Son kaç gün içinde yüklendiği bölümü
        if (activationList.get(18)) {
            link.append("date=").append(lastXDays).append("&");
        }
        //Yıl max bölümü
        if (activationList.get(6)) {
            int year = car.getYear(), yearMax;
            yearMax = Math.min(ranges[0] + year, LocalDate.now().getYear());
            link.append("a5_max=").append(yearMax).append("&");
        }
        //KM bölümü fixlenecek min km
        if (activationList.get(7)) {
            int kmMin = car.getKm() - ranges[1];
            if (kmMin < 0) {
                kmMin = 0;
            }
            link.append("a4_min=").append(kmMin).append("&");
        }
        //Fiyat bölümü fixlenecek min fiyat
        if (activationList.get(8)) {
            int priceMin = car.getPrice() - ranges[2];
            if (priceMin < 0) {
                priceMin = 0;
            }
            link.append("price_min=").append(priceMin).append("&");
        }
        link.append(first.toString());
        if (activationList.get(19)) {
            link.append("unpaintedParts=true").append("&");
        }
        //KM bölümü fixlenecek max km
        if (activationList.get(7)) {
            int kmMax = car.getKm() + ranges[1];
            link.append("a4_max=").append(kmMax).append("&");
        }
        if (activationList.get(17)) {
            link.append("sorting=date_desc").append("&");
        }

        //Yıl min için oluşturuldu
        if (activationList.get(6)) {
            int yearMin = car.getYear() - ranges[0];
            if (yearMin < 0) {
                yearMin = 0;
            }
            link.append("a5_min=").append(yearMin).append("&");
        }
        //İçerik ekleme bölümü
        if (activationList.get(16)) {
            link.append("query_desc=true").append("&");
        }
        link.append(second.toString());
        //Kelime arama bölümü 1
        if (activationList.get(15)) {
            link.append("query_text_mf=").append(keyWord).append("&");
        }
        link.append(third.toString());
        //Takas bölümü
        if (activationList.get(14)) {
            link.append("exchange=yes").append("&");
        }
        //Fiyat bölümü fixlenecek max fiyat
        if (activationList.get(8)) {
            int priceMax = car.getPrice() + ranges[2];
            link.append("price_max=").append(priceMax).append("&");
        }
        //Kelime arama için 2. bölüm
        if (activationList.get(15)) {
            link.append("query_text=").append(keyWord).append("&");
        }
        //Son karakter silindi.
        if (link.length() > 0) {
            link.setLength(link.length() - 1);
        }
        return formatText(link.toString()).replace("unpaintedparts", "unpaintedParts").replace("pagingsize", "pagingSize");
    }

    public static String calculateLaptopLink(Laptop laptop, LinkedList<DataBase> dataBases, ArrayList<Boolean> activationList, String keyWord, String lastXDays, int[] ranges, boolean[] processorValues) {

        StringBuilder link = new StringBuilder();
        link.append("https://www.sahibinden.com/");
        //Marka Bölümü
        if (activationList.get(21)) {
            if (laptop.getBrand().equals("")) {
                link.append(laptop.getBrand()).append("diger-laptop");
            } else {
                link.append(laptop.getBrand()).append("-laptop");
            }
        } else {
            link.append("laptop-bilgisayar");
        }
        if (activationList.get(4)) {
            link.append("/").append(laptop.getStatus());
        }
        //İlk Parçanın Sonu
        link.append("?");
        if (activationList.get(20)) {
            link.append("pagingSize=50").append("&");
        }
        //Son kaç gün içinde yüklendiği bölümü
        if (activationList.get(18)) {
            link.append("date=").append(lastXDays).append("&");
        }

        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        StringBuilder third = new StringBuilder();
        String moddedProcessor = Tools.modeProcessor(laptop.getProcessor(), processorValues);
        for (DataBase x : dataBases) {
            first.append(findMatch(x, ssdLaptopLookName, laptop.getSsd(), activationList.get(25), true));
            first.append(findMatch(x, processorLaptopLookName, moddedProcessor, activationList.get(22), false));
            first.append(findMatch(x, warrantyLookName, laptop.getWarranty(), activationList.get(10), false));
            second.append(findMatch(x, ramLaptopLookName, laptop.getRam(), activationList.get(23), true));
            third.append(findMatch(x, screenSizeLaptopLookName, laptop.getScreenSize(), activationList.get(27), false));
            third.append(findMatch(x, hddLaptopLookName, laptop.getHdd(), activationList.get(24), true));
            third.append(findMatch(x, gpuLaptopLookName, laptop.getGpu(), activationList.get(26), true));
            third.append(findMatch(x, resolutionLaptopLookName, laptop.getResolution(), activationList.get(28), false));
            second.append(findMatch(x, sellerLaptopLookName, laptop.getSeller(), activationList.get(5), false));

        }
        link.append(first.toString());
        //Kelime arama bölümü 1
        //Fiyat bölümü fixlenecek min fiyat
        if (activationList.get(8)) {
            int priceMin = laptop.getPrice() - ranges[2];
            if (priceMin < 0) {
                priceMin = 0;
            }
            link.append("price_min=").append(priceMin).append("&");
        }
        if (activationList.get(15)) {
            link.append("query_text_mf=").append(keyWord).append("&");
        }
        link.append(second.toString());
        if (activationList.get(17)) {
            link.append("sorting=date_desc").append("&");
        }
        //İçerik ekleme bölümü
        if (activationList.get(16)) {
            link.append("query_desc=true").append("&");
        }
        //Takas bölümü
        if (activationList.get(14)) {
            link.append("exchange=yes").append("&");
        }
        link.append(third.toString());
        //Fiyat bölümü fixlenecek max fiyat
        if (activationList.get(8)) {
            int priceMax = laptop.getPrice() + ranges[2];
            link.append("price_max=").append(priceMax).append("&");
        }
        //Kelime arama için 2. bölüm
        if (activationList.get(15)) {
            link.append("query_text=").append(keyWord).append("&");
        }
        //Son karakter silindi.
        if (link.length() > 0) {
            link.setLength(link.length() - 1);
        }
        return formatText(link.toString()).replace("pagingsize", "pagingSize");
    }

    private static String modeProcessor(String processorName, boolean[] value) {
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


    private static String findMatch(DataBase data, String titleFind, String textFind, boolean enabled, boolean identical) {
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

    private static ArrayList<Integer> findNumbers(String text) {
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
