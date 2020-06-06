package com.valinor61.sahibinden.car;

import com.valinor61.sahibinden.toolkit.Tools;

public class DetailInfo {
    protected String brand;
    protected String model;
    protected String warranty;
    protected String seller;
    protected int price;

    public DetailInfo(String brand, String model, String warranty, String seller, int price) {
        this.brand = brand;
        this.model = model;
        this.warranty = warranty;
        this.seller = seller;
        this.price = price;
    }

    public String getBrand() {
        return Tools.formatText(brand);
    }

    public String getModel() {
        return Tools.formatText(model);
    }

    public String getBrandValue() {
        return brand.replace("&nbsp;", "");
    }

    public String getModelValue() {
        return model.replace("&nbsp;", "");
    }

    public String getWarranty() {
        return warranty;
    }

    public String getSeller() {
        return seller;
    }

    public int getPrice() {
        return price;
    }

}
