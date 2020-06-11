package com.valinor61.sahibinden;

import com.valinor61.sahibinden.car.Car;
import com.valinor61.sahibinden.car.Laptop;
import com.valinor61.sahibinden.connection.Firefox;
import com.valinor61.sahibinden.data.DataBase;
import com.valinor61.sahibinden.data.Settings;
import com.valinor61.sahibinden.dialog.SettingsController;
import com.valinor61.sahibinden.list.CarList;
import com.valinor61.sahibinden.list.LaptopList;
import com.valinor61.sahibinden.list.TableList;
import com.valinor61.sahibinden.toolkit.Tools;
import com.valinor61.sahibinden.toolkit.extra.NumberString;
import com.valinor61.sahibinden.toolkit.extra.Ram;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Controller {
    private static final ObservableList<String> lastXDays = FXCollections.observableArrayList(new ArrayList<>(Arrays.asList("Son 1 gün", "Son 3 gün", "Son 7 gün", "Son 15 gün", "Son 30 gün")));
    private static String laptopSourceUrl;
    private static String carSourceUrl;
    private static String mainUrl;
    private static CheckBox[] comboBoxes;
    @FXML
    private ToggleGroup groupFirefox, groupImage, groupSearch;
    @FXML
    private ToggleButton filterButton;
    @FXML
    private Button calculateButton, stopButton;
    @FXML
    private MenuButton menuButton;
    @FXML
    private Tooltip tooltipToggle;
    private LinkedList<TableList> localListTemp, fullListTemp;
    private ObservableList<TableList> finalList, localList;
    private LinkedList<DataBase> dataBaseCar, dataBaseLaptop;
    private int[] ranges;
    private boolean[] processorValues;
    private boolean working = true, supportedMain = true, supportedSub = true, noError = false, detail = false;
    @FXML
    private BorderPane mainPane;
    @FXML
    private CheckBox series, model, fuel, gear, status, year, km, price, plate, power, color, chasis, unpainted;
    @FXML
    private CheckBox brandLaptop, processorLaptop, ramLaptop, hddLaptop, ssdLaptop, gpuLaptop, screenSizeLaptop, screenResolutionLaptop, brandProcessorLaptop, seriesProcessorLaptop, generationProcessorLaptop;
    @FXML
    private CheckBox seller, warranty, exchange, lastXDay, newToOld, searchText, searchContext, pageSize, engine, horsePower;
    @FXML
    private TextField keyword;
    @FXML
    private Label logArea, tableInfo, countArea;
    @FXML
    private ComboBox<String> lastXDayValue;
    @FXML
    private TextField urlField, yearField, kmField, priceField;
    @FXML
    private TableView<TableList> table;
    @FXML
    private TableColumn<TableList, ImageView> tableImage;
    @FXML
    private TableColumn<TableList, String> tableTitle;
    @FXML
    private TableColumn<TableList, String> tableModel;
    @FXML
    private TableColumn<TableList, String> tableSpecs1;
    @FXML
    private TableColumn<TableList, NumberString> tableSpecs2;
    @FXML
    private TableColumn<TableList, String> tableSpecs3;
    @FXML
    private TableColumn<TableList, String> tablePostDate;
    @FXML
    private TableColumn<TableList, String> tableLocation;
    @FXML
    private TableColumn<TableList, NumberString> tablePrice;
    @FXML
    private TableColumn<TableList, NumberString> tableDifference;
    @FXML
    private TableColumn<TableList, String> tableAverage;
    @FXML
    private Accordion accordion;
    @FXML
    private TitledPane carPanel;
    @FXML
    private Menu imageUpdateSpeed, searchUpdateSpeed;
    @FXML
    private RadioMenuItem visibleFirefox, outScreenFirefox, hiddenScreenFirefox;
    @FXML
    private RadioMenuItem zeroImageSpeed, veryfastImageSpeed, fastImageSpeed, regularImageSpeed, slowImageSpeed, verySlowImageSpeed, showImages;
    @FXML
    private RadioMenuItem veryfastSearchSpeed, fastSearchSpeed, regularSearchSpeed, slowSearchSpeed, verySlowSearchSpeed;

    public static String getCarSourceUrl() {
        return carSourceUrl;
    }

    public static void setCarSourceUrl(String carSourceUrl) {
        Controller.carSourceUrl = carSourceUrl;
    }

    public static String getMainUrl() {
        return mainUrl;
    }

    public static void setMainUrl(String mainUrl) {
        Controller.mainUrl = mainUrl;
    }

    public static String getLaptopSourceUrl() {
        return laptopSourceUrl;
    }

    public static void setLaptopSourceUrl(String laptopSourceUrl) {
        Controller.laptopSourceUrl = laptopSourceUrl;
    }

    public void initialize() {
        comboBoxes = new CheckBox[]{series, model, fuel, gear, status, seller, year, km, price, plate,
                /*10*/  warranty, power, color, chasis, exchange, searchText, searchContext, newToOld, lastXDay, unpainted,
                /*20*/  pageSize, brandLaptop, processorLaptop, ramLaptop, hddLaptop, ssdLaptop, gpuLaptop, screenSizeLaptop,
                /*28*/  screenResolutionLaptop, engine, horsePower, brandProcessorLaptop, seriesProcessorLaptop, generationProcessorLaptop};
        dataBaseCar = DataBase.readDataBase("data/databaseCar.dat");
        dataBaseLaptop = DataBase.readDataBase("data/databaseLaptop.dat");
        localListTemp = new LinkedList<>();
        fullListTemp = new LinkedList<>();
        finalList = FXCollections.observableArrayList();
        localList = FXCollections.observableArrayList();
        ranges = new int[]{0, 0, 0};
        processorValues = new boolean[]{false, false, false};
        readSettings();
        lastXDayValue.setItems(lastXDays);
        accordion.setExpandedPane(carPanel);
        accordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> observable, TitledPane oldPane, TitledPane newPane) -> {
            boolean expand = true; // This value will change to false if there's (at least) one pane that is in "expanded" state, so we don't have to expand anything manually
            for (TitledPane pane : accordion.getPanes()) {
                if (pane.isExpanded()) {
                    expand = false;
                }
            }
            /* Here we already know whether we need to expand the old pane again */
            if ((expand) && (oldPane != null)) {
                Platform.runLater(() -> accordion.setExpandedPane(oldPane));
            }
        });
        handleImageSpeed();
        handleSearchSpeed();
        handleFilterExtras();
        handleProcessorToggle();
        initializeTable();
        initializeCarTable();
        initializeTextFieldNumberOnly();
        initializeIcons();


    }

    private void initializeTable() {
        tableImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        tableModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tablePostDate.setCellValueFactory(new PropertyValueFactory<>("dateFull"));
        tableTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        tableLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        tableAverage.setCellValueFactory(new PropertyValueFactory<>("average"));
        tableDifference.setCellValueFactory(new PropertyValueFactory<>("difference"));
        initializeCarTable();
        if (filterButton.isSelected())
            table.setItems(localList);
        else {
            table.setItems(finalList);
        }
    }

    private void initializeCarTable() {
        tableModel.setText("Model");
        tableModel.setMinWidth(125);
        tableModel.setPrefWidth(125);
        tableSpecs1.setCellValueFactory(new PropertyValueFactory<>("year"));
        tableSpecs1.setText("Yıl");
        tableSpecs1.setMinWidth(75);
        tableSpecs1.setPrefWidth(75);

        tableSpecs2.setCellValueFactory(new PropertyValueFactory<>("km"));
        tableSpecs2.setText("Km");
        tableSpecs2.setMinWidth(75);
        tableSpecs2.setPrefWidth(75);


        tableSpecs3.setCellValueFactory(new PropertyValueFactory<>("color"));
        tableSpecs3.setText("Renk");
        tableSpecs3.setMinWidth(75);
        tableSpecs3.setPrefWidth(75);

    }

    private void initializeLaptopTable() {
        tableModel.setText("Marka");
        tableModel.setMinWidth(90);
        tableModel.setPrefWidth(90);
        tableSpecs1.setCellValueFactory(new PropertyValueFactory<>("processor"));
        tableSpecs1.setText("İşlemci");
        tableSpecs1.setMinWidth(100);
        tableSpecs1.setPrefWidth(100);

        tableSpecs2.setCellValueFactory(new PropertyValueFactory<>("ram"));
        tableSpecs2.setText("Ram");
        tableSpecs2.setMinWidth(60);
        tableSpecs2.setPrefWidth(60);

        tableSpecs3.setCellValueFactory(new PropertyValueFactory<>("screenSize"));
        tableSpecs3.setText("Ekran Boyutu");
        tableSpecs3.setMinWidth(100);
        tableSpecs3.setPrefWidth(100);

    }

    //Updates datebase files
    private void updateDatebase() {
        Runnable task = () -> {
            if (!Firefox.isOperates()) {
                Platform.runLater(() -> logArea.setText("Firefox Başlatılıyor."));
                Firefox.createFirefox(mainUrl, outScreenFirefox.isSelected(), hiddenScreenFirefox.isSelected());
            }
            Platform.runLater(() -> logArea.setText("Veritabanı Eksik Güncelleniyor."));
            Firefox.updateUrl(carSourceUrl, true);
            DataBase.generateDatabase(Firefox.getHtmlInformation(), "data/databaseCar.dat");
            dataBaseCar = DataBase.readDataBase("data/databaseCar.dat");
            Firefox.updateUrl(laptopSourceUrl, true);
            DataBase.generateDatabase(Firefox.getHtmlInformation(), "data/databaseLaptop.dat");
            dataBaseLaptop = DataBase.readDataBase("data/databaseLaptop.dat");
            Platform.runLater(() -> logArea.setText("Veritabanı Güncellendi. Şimdi arama yapabilirsiniz."));
        };
        new Thread(task).start();
    }

    private void readRangeValues() {
        ranges[0] = Tools.onlyNumbers(yearField.getText());
        ranges[1] = Tools.onlyNumbers(kmField.getText());
        ranges[2] = Tools.onlyNumbers(priceField.getText());
    }

    private void readProcessorValues() {
        processorValues[0] = brandProcessorLaptop.isSelected();
        processorValues[1] = seriesProcessorLaptop.isSelected();
        processorValues[2] = generationProcessorLaptop.isSelected();
    }

    private ArrayList<Boolean> getActivationArray() {
        return Arrays.stream(comboBoxes).map(CheckBox::isSelected).collect(Collectors.toCollection(() -> new ArrayList<>(Settings.actionListSize)));
    }

    private void setActivationArray(ArrayList<Boolean> activationSet) {
        IntStream.range(0, activationSet.size()).forEach(i -> comboBoxes[i].setSelected(activationSet.get(i)));
        handleKeywordSearch();
        handleLastXDay();
        updateRangeValues();
    }

    private void updateRangeValues() {
        yearField.setText(String.valueOf(ranges[0]));
        kmField.setText(String.valueOf(ranges[1]));
        priceField.setText(String.valueOf(ranges[2]));
    }

    //Speed Functions
    private int getSystemSpeed() {
        if (veryfastSearchSpeed.isSelected()) return 1000;
        else if (fastSearchSpeed.isSelected()) return 2000;
        else if (regularSearchSpeed.isSelected()) return 3000;
        else if (slowSearchSpeed.isSelected()) return 5000;
        else if (verySlowSearchSpeed.isSelected()) return 10000;
        else return 2000;
    }

    private void readSystemSpeed(int valueSpeed) {
        if (valueSpeed == 1000) veryfastSearchSpeed.setSelected(true);
        else if (valueSpeed == 2000) fastSearchSpeed.setSelected(true);
        else if (valueSpeed == 3000) regularSearchSpeed.setSelected(true);
        else if (valueSpeed == 5000) slowSearchSpeed.setSelected(true);
        else if (valueSpeed == 10000) verySlowSearchSpeed.setSelected(true);
        else fastSearchSpeed.setSelected(true);
    }

    private int getImageSpeed() {
        if (zeroImageSpeed.isSelected()) return 0;
        else if (veryfastImageSpeed.isSelected()) return 1;
        else if (fastImageSpeed.isSelected()) return 2;
        else if (regularImageSpeed.isSelected()) return 3;
        else if (slowImageSpeed.isSelected()) return 4;
        else if (verySlowImageSpeed.isSelected()) return 5;
        else return 6;
    }

    private void readImageSpeed(int valueSpeed, boolean valueActivate) {
        if (valueActivate) showImages.setSelected(true);
        if (valueSpeed == 0) zeroImageSpeed.setSelected(true);
        else if (valueSpeed == 1) veryfastImageSpeed.setSelected(true);
        else if (valueSpeed == 3) regularImageSpeed.setSelected(true);
        else if (valueSpeed == 4) slowImageSpeed.setSelected(true);
        else if (valueSpeed == 5) verySlowImageSpeed.setSelected(true);
        else fastImageSpeed.setSelected(true);
    }

    //Calculation function
    @FXML
    private void handleCalculateLinks() {
        working = true;
        countArea.setText("");
        //Reading year, km and price boxes
        readRangeValues();
        //Reading processor settings;
        readProcessorValues();
        //Checking Database
        if (dataBaseCar.isEmpty() || dataBaseLaptop.isEmpty()) {
            noError = true;
            updateDatebase();
            return;
        }
        //Starting task
        Runnable task = () -> {
            //If firefox not working initialize
            if (!Firefox.isOperates()) {
                Platform.runLater(() -> logArea.setText("Firefox Başlatılıyor."));
                Firefox.createFirefox(mainUrl, outScreenFirefox.isSelected(), hiddenScreenFirefox.isSelected());
            }
            Platform.runLater(() -> logArea.setText("Link Analiz Ediliyor."));
            //Clearing Previous List in case of previous search
            localListTemp.clear();
            localList.clear();
            fullListTemp.clear();
            finalList.clear();
            //Getting URL from text area
            String url = urlField.getText();
            //Checking web page correct or not
            if (url.contains(Firefox.getWebPage())) {
                //Checking if user stopped search
                if (working) {
                    //Accessing page
                    Firefox.updateUrl(url, true);
                    //Getting HTML information
                    String htmlContent = Firefox.getHtmlInformation();
                    //Reading info for checking detailed web page confirmation
                    String info = Tools.getInfo(htmlContent);
                    //Printing Table Text
                    Platform.runLater(() -> tableInfo.setText("Sonuçlar Hesaplanıyor"));
                    //Checking if Link is a carLink
                    if (info.contains("vasita")) {
                        //Initialize Table View
                        Platform.runLater(this::initializeCarTable);
                    }
                    //Checking if Link is a Laptop
                    else if (info.contains("bilgisayar")) {
                        //Initialize Table View
                        Platform.runLater(this::initializeLaptopTable);
                    } else {
                        Platform.runLater(() -> tableInfo.setText("Sonuç Bulunamadı."));
                        //Setting a boolean for not supported Link
                        supportedMain = false;
                    }
                    if (supportedMain) {
                        //Calculating Final List
                        calculateFinalList(url, htmlContent);
                    }
                }
                //Make all list elements unique
                LinkedList<TableList> temp = new LinkedList<>();
                for (TableList x : fullListTemp) {
                    int i = 0;
                    while (i < temp.size() && !temp.get(i).equals(x)) {
                        i++;
                    }
                    if (i == temp.size()) {
                        temp.add(x);
                    }
                }
                //Update images if this feature enabled or system not interrupted.
                if (showImages.isSelected() && working) {
                    Platform.runLater(() -> logArea.setText("Resimler Güncelleniyor."));
                    for (TableList x : temp) {
                        x.updateImage();
                    }
                    for (TableList x : localListTemp) {
                        x.updateImage();
                    }
                }
                //Update local list element if there is no calculated average.
                for (TableList x : localListTemp) {
                    if (x.getAverageValue() == 0) {
                        x.setAverage(x.getPrice().getPriceValue());
                    }
                }
                //Set all items for display
                finalList.setAll(temp);
                localList.setAll(localListTemp);
                //Update images from links for obtained list

                //If no result then print table text
                if (finalList.isEmpty() || localList.isEmpty()) {
                    Platform.runLater(() -> tableInfo.setText("Sonuç Bulunamadı."));
                }
                if (filterButton.isSelected() && !detail) {
                    if (localList.isEmpty()) {
                        Platform.runLater(() -> countArea.setText("Eşleşme Yok."));
                    } else {
                        Platform.runLater(() -> countArea.setText(localList.size() + " Adet Eşleşme Bulundu."));
                    }
                } else {
                    if (finalList.isEmpty()) {
                        Platform.runLater(() -> countArea.setText("Eşleşme Yok."));
                    } else {
                        Platform.runLater(() -> countArea.setText(finalList.size() + " Adet Eşleşme Bulundu."));
                    }
                }

                //Print correct informations for System Info
                if (supportedMain && supportedSub) {
                    if (working) {
                        Firefox.updateUrl(mainUrl, false);
                        Platform.runLater(() -> logArea.setText("İşlem Tamamlandı."));
                    } else {
                        Platform.runLater(() -> logArea.setText("İşlem Durduruldu."));
                    }
                } else {
                    if (!supportedMain) {
                        Platform.runLater(() -> logArea.setText("Bu Kategori Desteklenmiyor."));
                    }
                    if (!supportedSub) {
                        Platform.runLater(() -> logArea.setText("Bu Alt Kategori Desteklenmiyor."));
                    }
                }
            }
            //If web page is incorrect print an error.
            else if (!noError) {
                Platform.runLater(() -> logArea.setText("Hatalı Link."));
                noError = false;
            }
            supportedMain = true;
            supportedSub = true;
            working = true;
        };
        new Thread(task).start();
    }

    private void detailCalculate(String htmlContent) {
        detail = true;
        filterButton.setSelected(false);
        handleFilterExtras();
        fullListTemp = new LinkedList<>();
        String info = Tools.getInfo(htmlContent);
        if (info.contains("vasita")) {
            Car newCar = Car.generateCar(htmlContent);
            Platform.runLater(() -> logArea.setText("Araç Kategorisi: Detay Linki Girildi."));
            String newLink = CarList.calculateCarLink(newCar, dataBaseCar, getActivationArray(), keyword.getText(), Tools.formatLastXEN(lastXDayValue.getSelectionModel().getSelectedItem()), ranges);
            if (working)
                Firefox.updateUrl(newLink, true);
            fullListTemp = CarList.generateLinkList(Firefox.getHtmlInformation(), info, getActivationArray());
            CarList.updateAverageList(fullListTemp, null);
        } else if (info.contains("bilgisayar") && info.contains("Laptop")) {
            Laptop newLaptop = Laptop.generateLaptop(htmlContent);
            Platform.runLater(() -> logArea.setText("Laptop Kategorisi: Detay Linki Girildi."));
            String newLink = LaptopList.calculateLaptopLink(newLaptop, dataBaseLaptop, getActivationArray(), keyword.getText(), Tools.formatLastXEN(lastXDayValue.getSelectionModel().getSelectedItem()), ranges, processorValues);
            if (working)
                Firefox.updateUrl(newLink, true);
            fullListTemp = LaptopList.generateLinkList(Firefox.getHtmlInformation(), info, getActivationArray());
            LaptopList.updateAverageList(fullListTemp, null);
        } else supportedSub = false;
    }

    private void listCalculate(String htmlContent) {
        detail = false;
        fullListTemp = new LinkedList<>();
        localListTemp = new LinkedList<>();
        String info = Tools.getInfo(htmlContent);
        LinkedList<TableList> listLinks;
        if (info.contains("vasita")) {
            Platform.runLater(() -> logArea.setText("Araç Kategorisi: Liste Linki Girildi."));
            listLinks = CarList.generateLinkList(htmlContent, info, getActivationArray());
            int i = 1;
            for (TableList x : listLinks) {
                if (working) {
                    String k = String.valueOf(i);
                    int percentageSmaller = i * 100 / listLinks.size() - 50 / listLinks.size();
                    if (percentageSmaller < 0)
                        percentageSmaller = 0;
                    int percentageBigger = i * 100 / listLinks.size();
                    String percentage1 = "% " + percentageSmaller;
                    String percentage2 = "% " + percentageBigger;
                    if (i != 1)
                        Platform.runLater(() -> logArea.setText(percentage1 + " - " + k + ". Araç İçin Veriler İnceleniyor."));
                    Firefox.updateUrl(x.getLink(), true);
                    if (Firefox.isOperates()) {
                        htmlContent = Firefox.getHtmlInformation();
                        info = Tools.getInfo(htmlContent);
                    }
                    if (info.contains("otomobil")) {
                        Car newCar = Car.generateCar(htmlContent);
                        String newLink = CarList.calculateCarLink(newCar, dataBaseCar, getActivationArray(), keyword.getText(), Tools.formatLastXEN(lastXDayValue.getSelectionModel().getSelectedItem()), ranges);
                        Platform.runLater(() -> logArea.setText(percentage2 + " - " + k + ". Araç İçin Ortalama Fiyat Hesaplanıyor."));
                        if (Firefox.isOperates()) {
                            Firefox.updateUrl(newLink, true);
                            LinkedList<TableList> singleList = CarList.generateLinkList(Firefox.getHtmlInformation(), info, getActivationArray());
                            CarList.updateAverageList(singleList, x);
                            x.setModel((newCar.getBrandValue().trim() + "\n" + newCar.getSeriesValue().trim() + "\n" + newCar.getModelValue().trim()).replace("-", " "));
                            x.setYear(String.valueOf(newCar.getYear()));
                            x.setKm(newCar.getKm());
                            String temp = newCar.getColor().replace("-", " ");
                            x.setColor(temp.substring(0, 1).toUpperCase() + temp.substring(1));
                            fullListTemp.addAll(singleList);
                            localListTemp.add(x);
                        }
                    } else {
                        Platform.runLater(() -> logArea.setText("Bu Alt Kategori Desteklenmiyor."));
                    }
                    i++;
                }
            }
        } else if (info.contains("bilgisayar")) {
            Platform.runLater(() -> logArea.setText("Laptop Kategorisi: Liste Linki Girildi."));
            listLinks = LaptopList.generateLinkList(htmlContent, info, getActivationArray());
            int i = 1;
            for (TableList x : listLinks) {
                if (working) {
                    String k = String.valueOf(i);
                    int percentageSmaller = i * 100 / listLinks.size() - 50 / listLinks.size();
                    if (percentageSmaller < 0) {
                        percentageSmaller = 0;
                    }
                    int percentageBigger = i * 100 / listLinks.size();
                    String percentage1 = "% " + percentageSmaller;
                    String percentage2 = "% " + percentageBigger;
                    if (i != 1)
                        Platform.runLater(() -> logArea.setText(percentage1 + " - " + k + ". Laptop İçin Veriler İnceleniyor."));
                    if (Firefox.isOperates()) {
                        Firefox.updateUrl(x.getLink(), true);
                        htmlContent = Firefox.getHtmlInformation();
                    }
                    info = Tools.getInfo(htmlContent);
                    if (info.contains("notebook") && info.contains("Laptop")) {
                        Laptop newLaptop = Laptop.generateLaptop(htmlContent);
                        String newLink = LaptopList.calculateLaptopLink(newLaptop, dataBaseLaptop, getActivationArray(), keyword.getText(), Tools.formatLastXEN(lastXDayValue.getSelectionModel().getSelectedItem()), ranges, processorValues);
                        Platform.runLater(() -> logArea.setText(percentage2 + " - " + k + ". Laptop İçin Ortalama Fiyat Hesaplanıyor."));
                        if (Firefox.isOperates()) {
                            Firefox.updateUrl(newLink, true);
                            LinkedList<TableList> singleList = LaptopList.generateLinkList(Firefox.getHtmlInformation(), info, getActivationArray());
                            LaptopList.updateAverageList(singleList, x);
                            x.setModel(newLaptop.getBrandValue() + "\n" + newLaptop.getModelValue());
                            x.setRam(new Ram(newLaptop.getRam().replace("-", " ").toUpperCase()));
                            x.setProcessor(newLaptop.getProcessorRegular());
                            x.setScreenSize(newLaptop.getScreenSize().replace("-", " "));
                            localListTemp.add(x);
                            fullListTemp.addAll(singleList);
                        }
                    } else {
                        Platform.runLater(() -> logArea.setText("Bu Alt Kategori Desteklenmiyor."));
                    }
                    i++;
                }
            }
        }
    }

    private void calculateFinalList(String url, String htmlContent) {
        if (Firefox.isOperates()) {
            if (url.contains("detay"))
                detailCalculate(htmlContent);
            else
                listCalculate(htmlContent);
        }

    }


    @FXML
    private void saveSettings() {
        ArrayList<Boolean> activations = getActivationArray();
        ranges[0] = Tools.onlyNumbers(yearField.getText());
        ranges[1] = Tools.onlyNumbers(kmField.getText());
        ranges[2] = Tools.onlyNumbers(priceField.getText());
        boolean[] firefox = new boolean[]{visibleFirefox.isSelected(), outScreenFirefox.isSelected(), hiddenScreenFirefox.isSelected()};
        Settings.writeActivationValues(activations, ranges, keyword.getText(), Tools.formatLastXEN(lastXDayValue.getSelectionModel().getSelectedItem()), firefox, getSystemSpeed(), getImageSpeed(), showImages.isSelected(), filterButton.isSelected());
    }

    private void saveOnlySettings() {
        boolean[] firefox = new boolean[]{visibleFirefox.isSelected(), outScreenFirefox.isSelected(), hiddenScreenFirefox.isSelected()};
        Settings.writeActivationValues(firefox, getSystemSpeed(), getImageSpeed(), showImages.isSelected(), filterButton.isSelected());
    }

    @FXML
    private void readSettings() {
        boolean[] isCorrectRead = new boolean[]{true};
        Settings readed = Settings.readActivationValues(isCorrectRead);
        System.out.println(readed);
        mainUrl = readed.getMainUrl();
        laptopSourceUrl = readed.getLaptopSourceUrl();
        carSourceUrl = readed.getCarSourceUrl();
        CarList.setStockLogo(readed.getStockLogo());
        Firefox.setUserAgent(readed.getUserAgent());
        ArrayList<Boolean> activationArray = readed.getActivationList();
        setActivationArray(activationArray);
        ranges = readed.getRanges();
        keyword.setText(readed.getKeyWord());
        lastXDayValue.getSelectionModel().select(Tools.formatLastXTR(readed.getLastXDay()));
        updateRangeValues();
        handleYearField();
        handleKMField();
        handlePriceField();
        if (readed.getFirefoxMenu() == 0) visibleFirefox.setSelected(true);
        if (readed.getFirefoxMenu() == 1) outScreenFirefox.setSelected(true);
        if (readed.getFirefoxMenu() == 2) hiddenScreenFirefox.setSelected(true);
        if (!isCorrectRead[0]) saveSettings();
        readSystemSpeed(readed.getSearchSpeed());
        readImageSpeed(readed.getImageSpeed(), readed.isShowImages());
        filterButton.setSelected(readed.isHideAllItems());
    }

    @FXML
    private void handleLastXDay() {
        lastXDayValue.setDisable(!lastXDay.isSelected());
    }

    @FXML
    private void handleKeywordSearch() {
        if (searchText.isSelected()) {
            keyword.setDisable(false);
            searchContext.setDisable(false);
        } else {
            searchContext.setSelected(false);
            searchContext.setDisable(true);
            keyword.setDisable(true);
        }
    }

    @FXML
    private void handleYearField() {
        yearField.setDisable(!year.isSelected());
    }

    @FXML
    private void handleKMField() {
        kmField.setDisable(!km.isSelected());
    }

    @FXML
    private void handlePriceField() {
        priceField.setDisable(!price.isSelected());
    }


    @FXML
    private void handleTableClick(MouseEvent event) {
        try {
            if (table.getSelectionModel().getSelectedCells().get(0).getColumn() == 0 || event.getClickCount() == 2) {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    try {
                        Desktop.getDesktop().browse(new URI(table.getSelectionModel().getSelectedItem().getLink()));
                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Boş Satır");
        }
    }


    @FXML
    private void handleFilterExtras() {
        saveOnlySettings();
        if (filterButton.isSelected() && !detail) {
            table.setItems(localList);
            tooltipToggle.setText("Tüm İlanları Göster");
            filterButton.getStyleClass().clear();
            filterButton.getStyleClass().add("icon-up");
            if (localList.isEmpty()) countArea.setText("");
            else countArea.setText(localList.size() + " Adet Eşleşme Bulundu.");
        } else {
            table.setItems(finalList);
            tooltipToggle.setText("Sadece Ana İlanları Göster");
            filterButton.getStyleClass().clear();
            filterButton.getStyleClass().add("icon-down");
            if (finalList.isEmpty()) countArea.setText("");
            else countArea.setText(finalList.size() + " Adet Eşleşme Bulundu.");
        }
        if (table.getItems().size() != 0) table.scrollTo(0);
    }

    @FXML
    private void handleProcessorToggle() {
        brandProcessorLaptop.setDisable(!processorLaptop.isSelected());
        seriesProcessorLaptop.setDisable(!processorLaptop.isSelected());
        generationProcessorLaptop.setDisable(!processorLaptop.isSelected());
    }

    @FXML
    private void handleAdvancedSettings() {
        //Yeni diyalog oluşturuldu.
        Dialog<ButtonType> dialog = new Dialog<>();
        //Oluşturulan diyalog ana ekrana bağlandı.
        dialog.initOwner(mainPane.getScene().getWindow());
        dialog.getDialogPane().setPrefWidth(300);
        dialog.getDialogPane().setPrefWidth(700);
        //Dialog title ve header eklendi.
        dialog.setTitle("Gelişmiş Seçenekler");
        dialog.setHeaderText("Sistem Ayarları");

        //fxml bilgisi fxmlLoadere kaydedildi bu sayede settings.fxml dosyasına bağlantı sağlandı.
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("dialog/settings.fxml"));
        try {
            //Fxml diyaloğu başlatıldı bu kod exception yollayabildiğinden try catch içine alındı.
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }
        //OK ve CANCEL butonları eklendi. Bu butonlar standart fx kütüphanesinden seçildi.
        ButtonType save = new ButtonType("Kaydet", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("İptal", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(save);
        dialog.getDialogPane().getButtonTypes().add(cancel);
        //Butonlar  için özellik atandı bu sayede butonlar için pencere açık tutuldu.
        Optional<ButtonType> result = dialog.showAndWait();
        //OK tuşuna tıklandı bilgisini aldık.
        if (result.isPresent() && result.get() == save) {
            SettingsController controller = fxmlLoader.getController();
            controller.processResult();
            saveSettings();
            System.out.println("Saving Settings Completed.");
        } else if (result.isPresent() && result.get() == cancel)
            System.out.println("Saving Settings Cancelled.");
    }

    @FXML
    private void handleFirefox() {
        Runnable task;
        saveOnlySettings();
        task = () -> {
            Firefox.destroyFirefox();
            if (!Firefox.isOperates())
                Firefox.createFirefox(mainUrl, outScreenFirefox.isSelected(), hiddenScreenFirefox.isSelected());
        };
        new Thread(task).start();
    }

    @FXML
    private void handleSearchSpeed() {
        saveOnlySettings();
        if (veryfastSearchSpeed.isSelected()) Firefox.setSearchUpdateSpeed(1000);
        else if (fastSearchSpeed.isSelected()) Firefox.setSearchUpdateSpeed(2000);
        else if (regularSearchSpeed.isSelected()) Firefox.setSearchUpdateSpeed(3000);
        else if (slowSearchSpeed.isSelected()) Firefox.setSearchUpdateSpeed(5000);
        else if (verySlowSearchSpeed.isSelected()) Firefox.setSearchUpdateSpeed(10000);
        else Firefox.setSearchUpdateSpeed(2000);
    }

    @FXML
    private void handleImageSpeed() {
        saveOnlySettings();
        imageUpdateSpeed.setVisible(showImages.isSelected());
        tableImage.setVisible(showImages.isSelected());
        if (showImages.isSelected()) {
            if (zeroImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(0);
            else if (veryfastImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(1);
            else if (fastImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(2);
            else if (regularImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(5);
            else if (slowImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(20);
            else if (verySlowImageSpeed.isSelected()) Firefox.setImageUpdateSpeed(100);
            else Firefox.setImageUpdateSpeed(10);
        }
    }

    //Stops Links
    @FXML
    private void handleStopLink() {
        working = false;
    }

    @FXML
    private void handleKeyboardEvent(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) handleCalculateLinks();
        if (event.getCode() == KeyCode.ESCAPE) handleStopLink();
    }

    @FXML
    private void initializeIcons() {
        calculateButton.getStyleClass().add("icon-calculate");
        calculateButton.setPickOnBounds(true);
        stopButton.getStyleClass().add("icon-cancel");
        stopButton.setPickOnBounds(true);
        filterButton.getStyleClass().add("icon-down");
        filterButton.setPickOnBounds(true);
        menuButton.getStyleClass().add("icon-settings");
        menuButton.setPickOnBounds(true);

    }

    private void textFieldOnlyNumbers(TextField x) {
        x.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                x.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void initializeTextFieldNumberOnly() {
        textFieldOnlyNumbers(yearField);
        textFieldOnlyNumbers(kmField);
        textFieldOnlyNumbers(priceField);
    }

}
