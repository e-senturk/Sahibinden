package com.valinor61.sahibinden.car;

import com.valinor61.sahibinden.toolkit.Tools;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Laptop extends DetailInfo {
    private final String processor;
    private final String ram;
    private final String hdd;
    private final String ssd;
    private final String gpu;
    private final String screenSize;
    private final String resolution;
    private final String status;

    public Laptop(String brand, String model, String processor, String ram, String hdd, String ssd, String gpu, String screenSize, String resolution, String warranty, String seller, int price, String status) {
        super(brand, model, warranty, seller, price);
        this.processor = processor;
        this.ram = ram;
        this.hdd = hdd;
        this.ssd = ssd;
        this.gpu = gpu;
        this.screenSize = screenSize;
        this.resolution = resolution;
        this.status = status;
    }

    @NotNull
    @Contract("_ -> new")
    public static Laptop generateLaptop(String url) {
        String longText = Tools.findBetween(url, "<span class=\"classifiedId\"", "classifiedIdBox", 0);
        //Marka oluşturuldu.
        String brand = Tools.findBetween(longText, "Marka</strong>&nbsp;\n" +
                "                <span>", "</span>", 0);
        //Model oluşturuldu.
        String model = Tools.findBetween(longText, "Model</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        //İşlemci oluşturuldu.
        String processor = Tools.findBetween(longText, "İşlemci</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        //Ram oluşturuldu.
        String ram = Tools.findBetween(longText, "RAM</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        ram = Tools.formatText(ram);
        //Durum oluşturuldu.
        String status = Tools.findBetween(longText, "Durumu</strong>&nbsp;\n" +
                "        <span>\n" +
                "            ", "</span>", 0);
        status = Tools.formatText(status);
        //Hdd oluşturuldu.
        String hdd = Tools.findBetween(longText, "Sabit Disk (HDD)</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0).trim();
        hdd = Tools.formatText(hdd);
        //Kasa Tipi oluşturuldu.
        String ssd = Tools.findBetween(longText, "Sabit Disk (SSD)</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        ssd = Tools.formatText(ssd);
        //Ekran kartı oluşturuldu.
        String gpu = Tools.findBetween(longText, "Ekran Kartı</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        gpu = Tools.formatText(gpu);
        //Ekran Boyutu oluşturuldu.
        String screenSize = Tools.findBetween(longText, "Ekran Boyutu</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        screenSize = Tools.formatText(screenSize);
        //Çözünürlük oluşturuldu.
        String resolution = Tools.findBetween(longText, "Çözünürlük</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        resolution = Tools.formatText(resolution);
        //Garanti bilgisi oluşturuldu.
        String warranty = Tools.findBetween(longText, "Garanti</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        warranty = Tools.formatText(warranty);
        //Satıcı bilgisi oluşturuldu.
        String seller = Tools.findBetween(longText, "Kimden</strong>&nbsp;\n" +
                "                <span class=\"fromOwner\">\n" +
                "                \t", "</span>", 0);
        if (seller.equals("")) {
            seller = Tools.findBetween(longText, "Kimden</strong>&nbsp;\n" +
                    "                <span class=\"\">\n" +
                    "                \t", "</span>", 0);
        }
        seller = Tools.formatText(seller);
        //Ürün fiyat bilgisi oluşturuldu.
        String price = Tools.findBetween(url, "\"classifiedInfo \">\n" +
                "\n" +
                "                    <h3>\n" +
                "                 ", "<input id=\"priceHistoryFlag\"", 0);
        price = Tools.formatText(price);
        return new Laptop(brand, model, processor, ram, hdd, ssd, gpu, screenSize, resolution, warranty, seller, Tools.onlyNumbers(price), status);
    }

    public String getStatus() {
        return status;
    }

    public String getProcessor() {
        return Tools.formatText(processor);
    }

    public String getProcessorRegular() {
        return processor;
    }

    public String getRam() {
        return ram;
    }

    public String getHdd() {
        return hdd;
    }

    public String getSsd() {
        return ssd;
    }

    public String getGpu() {
        return gpu;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public String getResolution() {
        return resolution;
    }
}
