package com.valinor61.sahibinden.list;

import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.Ram;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.LinkedList;

public class LaptopList extends TableList {
    private String processor;
    private Ram ram;
    private String screenSize;

    public LaptopList(String model, String title, String imageUrl, ArrayList<String> specs, String price, String location, String link, ArrayList<String> date) {
        super(model, title, imageUrl, price, date, location, link);
        if (specs.size() >= 3) {
            this.screenSize = specs.get(2);
        }
        if (specs.size() >= 2) {
            this.ram = new Ram(specs.get(1));
        }
        if (specs.size() >= 1) {
            this.processor = specs.get(0);
        }
    }

    //Araba listesinden her bir linki çeken fonksiyon
    public static LinkedList<TableList> generateLinkList(String fullURL, String info, ArrayList<Boolean> activationList) {
        LinkedList<String> elements = Tools.findBetweenList(fullURL, "LargeThumbnail", "data-content=\"Göster\">");
        LinkedList<TableList> laptopLists = new LinkedList<>();
        for (String x : elements) {
            String model;
            if (activationList.get(21)) {
                model = calculateLaptopModel(info, 0, 10);
            } else {
                model = Tools.findBetween(x, "searchResultsTagAttributeValue\">\n" +
                        "                        ", "</td>", 0);
            }

            String title = Tools.findBetween(x, "title=\"", "\"", 0);
            String imageUrl = Tools.findBetween(x, "img class", "alt", 0);
            imageUrl = Tools.findBetween(imageUrl, "src=\"", "\"", 0);
            ArrayList<String> specs = new ArrayList<>(Tools.findBetweenList(x, "searchResultsAttributeValue\">", "</td>"));
            String price = Tools.findBetween(x, "<div> ", "<", 0);
            ArrayList<String> date = new ArrayList<>(Tools.findBetweenList(x, "<span>", "</span>"));
            String location = Tools.findBetween(x, "searchResultsLocationValue\">", "</td>", 0);
            String link = Tools.findBetween(x, "<a href=\"/", "\"", 0);
            LaptopList newElement = new LaptopList(model, title, imageUrl, specs, price, location, link, date);

            laptopLists.add(newElement);
        }
        return laptopLists;
    }

    public static String calculateLaptopModel(String info, int start, int end) {
        StringBuilder modelBuilder = new StringBuilder();
        String model = "";
        if (!info.equals("")) {
            LinkedList<String> modelList = Tools.findBetweenList(info, ">\n" +
                    "                    ", "</a>\n" +
                    "            </li>");
            int i = 0;
            for (String y : modelList) {
                if (i >= start && i <= end) {
                    if (!y.trim().equals("Laptop") && !y.trim().equals("Bilgisayar") && !y.trim().equals("Dizüstü (Notebook)") && !y.trim().equals("İkinci El ve Sıfır Alışveriş")) {
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
    public String getProcessor() {
        return processor;
    }

    @Override
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    @FXML
    public Ram getRam() {
        return ram;
    }

    @Override
    public void setRam(Ram ram) {
        this.ram = ram;
    }

    @FXML
    public String getScreenSize() {
        return screenSize;
    }

    @Override
    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String toString() {
        return "Model: " + model + "\n" +
                "Title: " + title + "\n" +
                "ImageURL: " + imageUrl + "\n" +
                "Processor: " + processor + "\n" +
                "Ram: " + ram + "\n" +
                "ScreenSize: " + screenSize + "\n" +
                "Price: " + price + "\n" +
                "Date: " + dateDay + " " + dateYear + "\n" +
                "Location: " + location + "\n" +
                "Link: " + link + "\n" +
                "Average: " + average + "\n" +
                "Difference: " + difference + "\n\n";
    }
}
