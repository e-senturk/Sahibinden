package com.valinor61.sahibinden.list;

import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.Kilometer;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.LinkedList;

public class CarList extends TableList {
    private String year;
    private Kilometer km;
    private String color;

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
