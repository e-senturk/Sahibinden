package com.valinor61.sahibinden.data;

import com.valinor61.sahibinden.Controller;
import com.valinor61.sahibinden.connection.Firefox;
import com.valinor61.sahibinden.list.CarList;
import com.valinor61.sahibinden.toolkit.Tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Settings {
    public static final int actionListSize = 34;
    private static final Settings defaultSettings = new Settings(new ArrayList<>(Collections.nCopies(actionListSize, false)),
            new int[]{0, 0, 0}, "", "1day", "https://www.google.com.tr/", "https://www.sahibinden.com/laptop-bilgisayar",
            "https://www.sahibinden.com/bmw-i-serisi-i8-premium-techno/benzin/otomatik/ikinci-el/sahibinden?date=1day&a5_max=2019&a4_min=10000&price_min=50000&a9620=143038&a2320=74346&a4054=72906&unpaintedParts=true&a4_max=200000&a5_min=2001&query_desc=true&hasMegaPhoto=true&a1864=51982&a9213=136271&query_text_mf=bmw&a3=33612&a8=250&exchange=yes&price_max=500000&query_text=bmw",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15\")",
            "https://image5.sahibinden.com/cms/files/sahi_images/Logo-1x.png",
            1, true, 2000, 2, false);
    private final ArrayList<Boolean> activationList;
    private final int[] ranges;
    private final String keyWord;
    private final String laptopSourceUrl;
    private final String carSourceUrl;
    private final String mainUrl;
    private final String userAgent;
    private final String stockLogo;
    private final int firefoxMenu;
    private final boolean showImages;
    private final int searchSpeed;
    private final int imageSpeed;
    private final boolean hideAllItems;
    private final String lastXDay;

    public Settings(ArrayList<Boolean> activationList, int[] ranges, String keyWord, String lastXDay, String mainUrl, String laptopSourceUrl, String carSourceUrl, String userAgent, String stockLogo, int firefoxMenu, boolean showImages, int searchSpeed, int imageSpeed, boolean hideAllItems) {
        this.activationList = activationList;
        this.ranges = ranges;
        this.keyWord = keyWord;
        this.laptopSourceUrl = laptopSourceUrl;
        this.carSourceUrl = carSourceUrl;
        this.mainUrl = mainUrl;
        this.userAgent = userAgent;
        this.lastXDay = lastXDay;
        this.stockLogo = stockLogo;
        this.firefoxMenu = firefoxMenu;
        this.showImages = showImages;
        this.imageSpeed = imageSpeed;
        this.hideAllItems = hideAllItems;
        this.searchSpeed = searchSpeed;
    }

    public static void writeActivationValues(ArrayList<Boolean> activationList, int[] ranges, String keyWord, String lastXDay, boolean[] firefox, int searchSpeed, int imageSpeed, boolean imageShown, boolean showAllItems) {
        try {
            int firefoxValue = 0;
            if (firefox[1])
                firefoxValue = 1;
            if (firefox[2])
                firefoxValue = 2;

            Path path = Paths.get("data/settings.ini");
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write("*Ana Ayarlar*");
            writer.newLine();
            writer.write("#Aktivasyon Değerleri :");
            writer.newLine();
            for (boolean x : activationList) {
                writer.write(String.format("%b\t", x));
            }

            writer.newLine();
            writer.write("#Aralık Değerleri :");
            writer.newLine();
            writer.write(String.format("%d\t%d\t%d\t", ranges[0], ranges[1], ranges[2]));

            writer.newLine();
            writer.write("#Kelime Değerleri :");
            writer.newLine();
            writer.write(String.format("%s\t", "-" + keyWord));
            writer.write(String.format("%s\t", "-" + lastXDay));
            writer.newLine();
            writer.write("#Ana Sayfa URL :");
            writer.newLine();
            writer.write("Main:");
            writer.write(Controller.getMainUrl());
            writer.newLine();
            writer.write("#Laptop Kaynak URL :");
            writer.newLine();
            writer.write("Laptop:");
            writer.write(Controller.getLaptopSourceUrl());
            writer.newLine();
            writer.write("#Araç Kaynak URL :");
            writer.newLine();
            writer.write("Car:");
            writer.write(Controller.getCarSourceUrl());
            writer.newLine();
            writer.write("#User Agent Modulü :");
            writer.newLine();
            writer.write(Firefox.getUserAgent());
            writer.newLine();
            writer.write("#Hatalı Resim :");
            writer.newLine();
            writer.write("Image:");
            writer.write(CarList.getStockLogo());
            writer.newLine();
            writer.newLine();
            writer.write("*Kullanıcı Ayarları*");
            writer.newLine();
            writeUserSettingsOnly(searchSpeed, imageSpeed, imageShown, firefoxValue, showAllItems, writer);
        } catch (IOException e) {
            System.out.println("Can't write settings");
        }
    }

    public static void writeActivationValues(boolean[] firefox, int searchSpeed, int imageSpeed, boolean imageShown, boolean showAllItems) {
        try {
            int firefoxValue = 0;
            if (firefox[1])
                firefoxValue = 1;
            if (firefox[2])
                firefoxValue = 2;

            Path path = Paths.get("data/settings.ini");
            BufferedReader reader = Files.newBufferedReader(path);
            StringBuilder readedText = new StringBuilder();
            String temp = "";
            while (!temp.equals("*Kullanıcı Ayarları*")) {
                temp = reader.readLine().trim();
                readedText.append(temp).append("\n");
            }
            reader.close();
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write(readedText.toString());
            writeUserSettingsOnly(searchSpeed, imageSpeed, imageShown, firefoxValue, showAllItems, writer);
        } catch (Exception e) {
            System.out.println("Can't write user settings");
        }
    }

    private static void writeUserSettingsOnly(int systemSpeed, int imageSpeed, boolean imageShown, int firefoxValue, boolean showAllItems, BufferedWriter writer) throws IOException {
        writer.write("#Firefox Görünüm Modu :");
        writer.newLine();
        writer.write(String.valueOf(firefoxValue));
        writer.newLine();
        writer.write("#Resim Görünümü :");
        writer.newLine();
        writer.write(String.valueOf(imageShown));
        writer.newLine();
        writer.write("#Site Güncelleme Hızı :");
        writer.newLine();
        writer.write(String.valueOf(systemSpeed));
        writer.newLine();
        writer.write("#Resim Güncelleme Hızı :");
        writer.newLine();
        writer.write(String.valueOf(imageSpeed));
        writer.newLine();
        writer.write("#Sadece Ana Listeyi Göster :");
        writer.newLine();
        writer.write(String.valueOf(showAllItems));
        writer.close();
    }

    public static Settings readActivationValues(boolean[] isCorrectRead) {
        try {
            int[] ranges = new int[]{0, 0, 0};
            ArrayList<Boolean> activationList = new ArrayList<>();
            String keyWord = "", lastXDay = "";
            Path path = Paths.get("data/settings.ini");
            BufferedReader reader = Files.newBufferedReader(path);
            reader.readLine();
            reader.readLine();
            String input = reader.readLine();
            String[] itemPieces = input.split("\t");
            if (itemPieces.length >= actionListSize) {
                for (int i = 0; i < actionListSize; i++) {
                    activationList.add(Boolean.parseBoolean(itemPieces[i]));
                }
            }
            reader.readLine();
            input = reader.readLine();
            itemPieces = input.split("\t");
            if (itemPieces.length >= 3) {
                ranges[0] = Tools.onlyNumbers(itemPieces[0]);
                ranges[1] = Tools.onlyNumbers(itemPieces[1]);
                ranges[2] = Tools.onlyNumbers(itemPieces[2]);
            }
            reader.readLine();
            input = reader.readLine();
            itemPieces = input.split("\t");
            if (itemPieces.length >= 2) {
                keyWord = itemPieces[0].replace("-", "");
                lastXDay = itemPieces[1].replace("-", "");
            }
            reader.readLine();
            String mainURL = reader.readLine().replace("Main:", "");
            reader.readLine();
            String laptopSourceUrl = reader.readLine().replace("Laptop:", "");
            reader.readLine();
            String carSourceUrl = reader.readLine().replace("Car:", "");
            reader.readLine();
            String userAgent = reader.readLine();
            reader.readLine();
            String stockLogo = reader.readLine().replace("Image:", "");
            reader.readLine();
            reader.readLine();
            reader.readLine();
            int firefoxMenu = Tools.onlyNumbers(reader.readLine());
            reader.readLine();
            boolean showImages = Boolean.parseBoolean(reader.readLine().trim());
            reader.readLine();
            int searchSpeed = Tools.onlyNumbers(reader.readLine());
            reader.readLine();
            int imageSpeed = Tools.onlyNumbers(reader.readLine());
            reader.readLine();
            boolean showAllItems = Boolean.parseBoolean(reader.readLine().trim());
            reader.close();

            return new Settings(activationList, ranges, keyWord, lastXDay, mainURL, laptopSourceUrl, carSourceUrl, userAgent, stockLogo, firefoxMenu, showImages, searchSpeed, imageSpeed, showAllItems);
        } catch (NullPointerException | IOException e) {
            if (isCorrectRead != null)
                isCorrectRead[0] = false;
            System.out.println("Failed to read settings intitialize defaults");
            return defaultSettings;
        }
    }

    public String getLaptopSourceUrl() {
        return laptopSourceUrl;
    }

    public boolean isShowImages() {
        return showImages;
    }

    public int getSearchSpeed() {
        return searchSpeed;
    }

    public int getImageSpeed() {
        return imageSpeed;
    }

    public String getStockLogo() {
        return stockLogo;
    }

    public String getCarSourceUrl() {
        return carSourceUrl;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public ArrayList<Boolean> getActivationList() {
        return activationList;
    }

    public int[] getRanges() {
        return ranges;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public String getLastXDay() {
        return lastXDay;
    }

    public int getFirefoxMenu() {
        return firefoxMenu;
    }

    public boolean isHideAllItems() {
        return hideAllItems;
    }

    @Override
    public String toString() {
        return "\t****SETTINGS**** \n" +
                "--General Settings-- \n" +
                "Activation Values : " + Arrays.toString(this.getActivationList().toArray()) + "\n" +
                "Ranges : " + Arrays.toString(this.ranges) + "\n" +
                "Keyword : " + this.keyWord + "\n" +
                "LastX : " + this.lastXDay + "\n" +
                "Laptop Source : " + this.laptopSourceUrl + "\n" +
                "Car Source : " + this.carSourceUrl + "\n" +
                "Main : " + this.mainUrl + "\n" +
                "StockLogo : " + this.stockLogo + "\n" +
                "UserAgent : " + this.userAgent + "\n\n" +
                "--User Settings--" + "\n" +
                "Firefox Visibility : " + this.firefoxMenu + "\n" +
                "Image Visibility : " + this.showImages + "\n" +
                "Page Search Speed : " + this.searchSpeed + "\n" +
                "Image Scan Speed : " + this.imageSpeed + "\n" +
                "Hide All Items : " + this.hideAllItems;
    }
}
