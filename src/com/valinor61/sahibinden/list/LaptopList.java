package com.valinor61.sahibinden.list;

import com.valinor61.sahibinden.car.Laptop;
import com.valinor61.sahibinden.data.DataBase;
import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.Ram;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.LinkedList;

public class LaptopList extends TableList {
    private String processor;
    private Ram ram;
    private String screenSize;
    private static final String processorLaptopLookName = "İşlemci";
    private static final String ramLaptopLookName = "RAM";
    private static final String hddLaptopLookName = "Sabit Disk (HDD)";
    private static final String ssdLaptopLookName = "Sabit Disk (SSD)";
    private static final String gpuLaptopLookName = "Ekran Kartı";
    private static final String screenSizeLaptopLookName = "Ekran Boyutu";
    private static final String resolutionLaptopLookName = "Çözünürlük";
    private static final String sellerLaptopLookName = "Kimden";
    private static final String warrantyLookName = "Garanti";


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
            first.append(Tools.findMatch(x, ssdLaptopLookName, laptop.getSsd(), activationList.get(25), true));
            first.append(Tools.findMatch(x, processorLaptopLookName, moddedProcessor, activationList.get(22), false));
            first.append(Tools.findMatch(x, warrantyLookName, laptop.getWarranty(), activationList.get(10), false));
            second.append(Tools.findMatch(x, ramLaptopLookName, laptop.getRam(), activationList.get(23), true));
            third.append(Tools.findMatch(x, screenSizeLaptopLookName, laptop.getScreenSize(), activationList.get(27), false));
            third.append(Tools.findMatch(x, hddLaptopLookName, laptop.getHdd(), activationList.get(24), true));
            third.append(Tools.findMatch(x, gpuLaptopLookName, laptop.getGpu(), activationList.get(26), true));
            third.append(Tools.findMatch(x, resolutionLaptopLookName, laptop.getResolution(), activationList.get(28), false));
            second.append(Tools.findMatch(x, sellerLaptopLookName, laptop.getSeller(), activationList.get(5), false));

        }
        link.append(first.toString());
        //Kelime arama bölümü 1
        //Fiyat bölümü fixlenecek min fiyat
        if (activationList.get(8)) {
            int priceMin;
            if (ranges[2] >= 100) {
                priceMin = 0;
            } else {
                priceMin = laptop.getPrice() - laptop.getPrice() * ranges[2] / 100;
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
            int priceMax = laptop.getPrice() + laptop.getPrice() * ranges[2] / 100;
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
        return Tools.formatText(link.toString()).replace("pagingsize", "pagingSize");
    }

    @FXML
    public String getProcessor() {
        return processor.replace("(", "\n(").replace("Intel ", "Intel\n").replace("AMD ", "AMD\n");
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

    @FXML
    @Override
    public String getModel() {
        return model.replace(" ", "\n");
    }
}
