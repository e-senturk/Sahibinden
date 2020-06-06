package com.valinor61.sahibinden.list;

import com.valinor61.sahibinden.connection.Firefox;
import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.Difference;
import com.valinor61.sahibinden.toolkit.extra.Price;
import com.valinor61.sahibinden.toolkit.extra.Ram;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.LinkedList;


public class TableList {
    protected static String stockLogo;
    protected String model;
    protected ImageView image;
    protected String imageUrl;
    protected String title;
    protected Price price;
    protected String location;
    protected String link;
    protected String dateDay;
    protected String dateYear;
    protected String dateFull;
    protected Price average;
    protected Difference difference;

    protected TableList(String model, String title, String imageUrl, String price, ArrayList<String> date, String location, String link) {
        this.model = model;
        this.title = title;
        if (imageUrl.trim().equals("")) {
            this.imageUrl = stockLogo;
        } else {
            this.imageUrl = imageUrl;
        }

        this.price = new Price(Tools.onlyNumbers(price));
        if (date.size() >= 2) {
            this.dateDay = date.get(0);
            this.dateYear = date.get(1);
            this.dateFull = dateDay + " " + dateYear;
        } else {
            this.dateDay = "Bulunamadı";
            this.dateYear = "Bulunamadı";
            this.dateFull = "Bulunamadı";
        }
        this.location = location.replace("<br>", "/");
        this.average = new Price(0);
        this.difference = new Difference(1, 1);

        this.link = Firefox.getWebPage() + link;
    }

    public static String getStockLogo() {
        return stockLogo;
    }

    public static void setStockLogo(String stockLogo) {
        CarList.stockLogo = stockLogo;
    }

    public static void updateAverageList(LinkedList<TableList> listTable, TableList mainElement) {

        int i = 0;
        long t = 0;
        for (TableList x : listTable) {
            t += x.price.getPriceValue();
            i++;
        }

        if (i != 0 && t != 0) {
            for (TableList x : listTable) {
                x.setAverage((int) (t / i));
                x.setDifference(x.price.getPriceValue() - Tools.onlyNumbers(x.getAverage()), (int) (t / i));
            }
            if (mainElement != null) {
                mainElement.setAverage((int) (t / i));
                mainElement.setDifference(mainElement.price.getPriceValue() - Tools.onlyNumbers(mainElement.getAverage()), (int) (t / i));
            }
        } else {
            for (TableList x : listTable) {
                x.setAverage(0);
                x.setDifference(0, 0);
            }
            if (mainElement != null) {
                mainElement.setAverage(0);
                mainElement.setDifference(0, 0);
            }
        }
    }

    @FXML
    public String getTitle() {
        return title;
    }

    @FXML
    public void setTitle(String title) {
        this.title = title;
    }

    @FXML
    public Price getPrice() {
        return price;
    }

    @FXML
    public String getLocation() {
        return location;
    }

    public String getLink() {
        return link;
    }

    @FXML
    public String getDateFull() {
        return dateFull;
    }

    @FXML
    public Difference getDifference() {
        return difference;
    }

    @FXML
    public ImageView getImage() {
        return image;
    }

    public void setDifference(int difference, int average) {
        this.difference = new Difference(difference, average);
    }

    public String getAverage() {
        return Tools.formatToTl(average.getPriceValue());
    }

    public void setAverage(int average) {
        this.average = new Price(average);
    }

    @FXML
    public String getImageUrl() {
        return imageUrl;
    }

    @FXML
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void updateImage() {
        if (Firefox.isOperates()) {
            this.image = new ImageView(Firefox.LoadImageFromClassAndSrcInfo(imageUrl, 124, 93));
        }
    }

    @Override
    public boolean equals(Object obj) {
        // If the object is compared with itself then return true
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof TableList)) {
            return false;
        }
        TableList table = (TableList) obj;
        return this.link.equals(table.getLink());
    }

    public void setYear(String year) {
    }

    public void setKm(int km) {
    }

    public void setColor(String color) {
    }

    public void setRam(Ram ram) {
    }

    public void setProcessor(String processor) {
    }

    public void setScreenSize(String screenSize) {
    }
}
