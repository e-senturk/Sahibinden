package com.valinor61.sahibinden.car;

import com.valinor61.sahibinden.toolkit.Tools;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Car extends DetailInfo {
    private final String series;
    private final String fuel;
    private final String gear;
    private final String status;
    private final int year;
    private final int km;
    private final String plate;
    private final String horsePower;
    private final String engine;
    private final String power;
    private final String color;
    private final String chasis;

    public Car(String brand, String series, String model, String fuel, String gear, String status, String seller, String year, String km, int price, String plate, String horsePower, String warranty, String engine, String power, String color, String chasis) {
        super(brand, model, warranty, seller, price);
        this.series = series;
        this.fuel = fuel;
        this.gear = gear;
        this.status = status;
        this.year = Tools.onlyNumbers(year);
        this.km = Tools.onlyNumbers(km);
        this.plate = plate;
        this.horsePower = horsePower;
        this.engine = engine;
        this.power = power;
        this.color = color;
        this.chasis = chasis;
    }

    @NotNull
    @Contract("_ -> new")
    public static Car generateCar(String url) {
        String longText = Tools.findBetween(url, "<span class=\"classifiedId\"", "classifiedIdBox", 0);
        //Marka oluşturuldu.
        String brand = Tools.findBetween(longText, "<strong>Marka</strong>&nbsp;\n" +
                "                <span>", "</span>", 0);
        //Seri oluşturuldu.
        String series = Tools.findBetween(longText, "Seri</strong>&nbsp;\n" +
                "                <span>", "</span>", 0);
        //Model oluşturuldu.
        String model = Tools.findBetween(longText, "Model</strong>&nbsp;\n" +
                "                <span>", "</span>", 0);
        //Yıl oluşturuldu.
        String year = Tools.findBetween(longText, "Yıl</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0).trim();
        //Yakıt oluşturuldu.
        String fuel = Tools.findBetween(longText, "Yakıt</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        fuel = Tools.formatText(fuel);
        //Vites oluşturuldu.
        String gear = Tools.findBetween(longText, "Vites</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        gear = Tools.formatText(gear);
        //Kilometre oluşturuldu.
        String km = Tools.findBetween(longText, "KM</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0).trim();
        //Kasa Tipi oluşturuldu.
        String chasis = Tools.findBetween(longText, "Kasa Tipi</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        chasis = Tools.formatText(chasis);
        //Motor gücü oluşturuldu.
        String horsePower = Tools.findBetween(longText, "Motor Gücü</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        horsePower = Tools.formatHorsePower(horsePower);
        //Motor hacmi oluşturuldu.
        String engine = Tools.findBetween(longText, "Motor Hacmi</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        //Çekiş gücü oluşturuldu.
        engine = Tools.formatEngine(engine);
        String power = Tools.findBetween(longText, "Çekiş</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        power = Tools.formatText(power);
        //Renk oluşturuldu.
        String color = Tools.findBetween(longText, "Renk</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        color = Tools.formatText(color);
        //Garanti bilgisi oluşturuldu.
        String warranty = Tools.findBetween(longText, "Garanti</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        warranty = Tools.formatText(warranty);
        //PLaka bilgisi oluşturuldu.
        String plate = Tools.findBetween(longText, "Plaka / Uyruk</strong>&nbsp;\n" +
                "                <span class=\"\">\n" +
                "                \t", "</span>", 0);
        plate = Tools.formatText(plate);
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
        //Ürün durumu bilgisi oluşturuldu.
        String status = Tools.findBetween(longText, "Durumu</strong>&nbsp;\n" +
                "        <span>\n" +
                "            ", "</span>", 0);
        status = Tools.formatText(status);
        //Ürün fiyat bilgisi oluşturuldu.
        String price = Tools.findBetween(url, "\"classifiedInfo \">\n" +
                "\n" +
                "                    <h3>\n" +
                "                 ", "<input id=\"priceHistoryFlag\"", 0);
        price = Tools.formatText(price);

        return new Car(brand, series, model, fuel, gear, status,
                seller, year, km, Tools.onlyNumbers(price), plate, horsePower, warranty,
                engine, power, color, chasis);
    }

    public String getSeries() {
        return Tools.formatText(series);
    }

    public String getSeriesValue() {
        return series.replace("&nbsp;", "");
    }

    public String getFuel() {
        return fuel;
    }

    public String getGear() {
        return gear;
    }

    public String getStatus() {
        return status;
    }

    public int getYear() {
        return year;
    }

    public int getKm() {
        return km;
    }

    public String getPlate() {
        return plate;
    }

    public String getPower() {
        return power;
    }

    public String getColor() {
        return color;
    }

    public String getChasis() {
        return chasis;
    }

    public String getHorsePower() {
        return this.horsePower;
    }

    public String getEngine() {
        return this.engine;
    }
}
