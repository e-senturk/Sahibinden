package com.valinor61.sahibinden.dialog;

import com.valinor61.sahibinden.Controller;
import com.valinor61.sahibinden.connection.Firefox;
import com.valinor61.sahibinden.data.Settings;
import com.valinor61.sahibinden.list.CarList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class SettingsController {
    @FXML
    private TextArea mainPageInput, sourceCarInput, sourceLaptopInput, userAgentInput, unknownImageInput;

    public void initialize() {
        Settings reader = Settings.readActivationValues(null);
        mainPageInput.setText(reader.getMainUrl());
        sourceCarInput.setText(reader.getCarSourceUrl());
        sourceLaptopInput.setText(reader.getLaptopSourceUrl());
        userAgentInput.setText(reader.getUserAgent());
        unknownImageInput.setText(reader.getStockLogo());

    }
    @FXML
    public void processResult() {
        Firefox.setUserAgent(userAgentInput.getText());
        Controller.setLaptopSourceUrl(sourceLaptopInput.getText());
        Controller.setCarSourceUrl(sourceCarInput.getText());
        Controller.setMainUrl(mainPageInput.getText());
        CarList.setStockLogo(unknownImageInput.getText());
    }

}
