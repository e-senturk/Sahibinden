package com.valinor61.sahibinden.list;

import com.valinor61.sahibinden.car.Car;
import com.valinor61.sahibinden.data.DataBase;
import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.Kilometer;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;

public class CarList extends TableList {
    private String year;
    private Kilometer km;
    private String color;
    private static final String plateLookName = "Plaka / Uyruk";
    private static final String horsePowerLookName = "Motor Gücü";
    private static final String warrantyLookName = "Garanti";
    private static final String engineLookName = "Motor Hacmi";
    private static final String powerLookName = "Çekiş";
    private static final String colorLookName = "Renk";
    private static final String chasisLookName = "Kasa Tipi";

    public CarList(String model, String title, String imageUrl, ArrayList<String> specs, String price, ArrayList<String> date, String location, String link) {
        super(model, title, imageUrl, price, date, location, link);
        if (specs.size() >= 3) {
            this.year = specs.get(0);
            this.km = new Kilometer(Tools.onlyNumbers(specs.get(1)));
            this.color = specs.get(2);
        } else {
            this.year = "Bulunamadı";
            this.km = new Kilometer(0);
            this.color = "Bulunamadı";
        }
    }

    //Araba listesinden her bir linki çeken fonksiyon
    public static LinkedList<TableList> generateLinkList(String fullURL, String info, ArrayList<Boolean> activationList) {
        LinkedList<String> elements = Tools.findBetweenList(fullURL, "LargeThumbnail", "data-content=\"Göster\">");
        LinkedList<TableList> carlists = new LinkedList<>();
        for (String x : elements) {
            StringBuilder modelBuilder = new StringBuilder();
            if (activationList.get(0) && activationList.get(1)) {
                modelBuilder.append(calculateCarModel(info, 2, 4));
                //Son karakter silindi.
                if (modelBuilder.length() > 0) {
                    modelBuilder.setLength(modelBuilder.length() - 1);
                }
                modelBuilder.append("  ");
            } else if (activationList.get(0)) {
                modelBuilder.append(calculateCarModel(info, 2, 3).trim()).append("\n");
            } else {
                modelBuilder.append(calculateCarModel(info, 2, 2).trim()).append("\n");
            }
            LinkedList<String> models = Tools.findBetweenList(x, "searchResultsTagAttributeValue\">\n" +
                    "                        ", "</td>");

            for (String t : models) {
                modelBuilder.append(t).append("\n");
            }
            //Son karakter silindi.
            if (modelBuilder.length() > 0) {
                modelBuilder.setLength(modelBuilder.length() - 1);
            }

            String model = modelBuilder.toString().replace("-", " ");

            String title = Tools.findBetween(x, "title=\"", "\"", 0);
            String imageUrl = Tools.findBetween(x, "img class", "alt", 0);
            imageUrl = Tools.findBetween(imageUrl, "src=\"", "\"", 0);
            ArrayList<String> specs = new ArrayList<>(Tools.findBetweenList(x, "\"searchResultsAttributeValue\">", "</td>"));
            String price = Tools.findBetween(x, "<div> ", "<", 0);
            ArrayList<String> date = new ArrayList<>(Tools.findBetweenList(x, "<span>", "</span>"));
            String location = Tools.findBetween(x, "searchResultsLocationValue\">", "</td>", 0);
            String link = Tools.findBetween(x, "<a href=\"/", "\"", 0);
            CarList newElement = new CarList(model, title, imageUrl, specs, price, date, location, link);
            carlists.add(newElement);
        }
        return carlists;
    }

    public static String calculateCarModel(String info, int start, int end) {
        StringBuilder modelBuilder = new StringBuilder();
        String model = "";
        if (!info.equals("")) {
            LinkedList<String> modelList = Tools.findBetweenList(info, ">\n" +
                    "                    ", "</a>\n" +
                    "            </li>");
            int i = 0;
            for (String y : modelList) {
                if (i >= start && i <= end) {
                    if (!y.trim().equals("Otomobil") && !y.trim().equals("Vasıta")) {
                        modelBuilder.append(y.trim()).append("\n");
                    }
                }
                i++;
            }
            model = modelBuilder.toString();
        }
        return model;
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
            first.append(Tools.findMatch(x, plateLookName, car.getPlate(), activationList.get(9), false));
            first.append(Tools.findMatch(x, horsePowerLookName, car.getHorsePower(), activationList.get(30), false));
            first.append(Tools.findMatch(x, warrantyLookName, car.getWarranty(), activationList.get(10), false));
            second.append(Tools.findMatch(x, engineLookName, car.getEngine(), activationList.get(29), false));
            second.append(Tools.findMatch(x, powerLookName, car.getPower(), activationList.get(11), false));
            third.append(Tools.findMatch(x, colorLookName, car.getColor(), activationList.get(12), false));
            third.append(Tools.findMatch(x, chasisLookName, car.getChasis(), activationList.get(13), false));
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
            int priceMin;
            if (ranges[2] >= 100) {
                priceMin = 0;
            } else {
                priceMin = car.getPrice() - car.getPrice() * ranges[2] / 100;
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
            int priceMax = car.getPrice() + car.getPrice() * ranges[2] / 100;
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
        return Tools.formatText(link.toString()).replace("unpaintedparts", "unpaintedParts").replace("pagingsize", "pagingSize");
    }

    @FXML
    public String getYear() {
        return year;
    }

    @Override
    public void setYear(String year) {
        this.year = year;
    }

    @FXML
    public Kilometer getKm() {
        return km;
    }

    @Override
    public void setKm(int km) {
        this.km = new Kilometer(km);
    }

    @FXML
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    //Yazdırmak için kullanılacak string
    @Override
    public String toString() {
        return "Model " + model + "\n" +
                "Title: " + title + "\n" +
                "ImageURL: " + imageUrl + "\n" +
                "Specs: " + year + "-" + km + "-" + color + "\n" +
                "Price: " + price + "\n" +
                "Date: " + dateDay + " " + dateYear + "\n" +
                "Location: " + location + "\n" +
                "Link: " + link;
    }
}
