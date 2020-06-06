module Sahibinden {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.desktop;
    requires selenium;

    opens com.valinor61.sahibinden;
    opens com.valinor61.sahibinden.dialog;
    opens com.valinor61.sahibinden.list;
}