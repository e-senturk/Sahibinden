package com.valinor61.sahibinden.connection;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.Base64;
import java.util.Objects;

public class Firefox {
    private static final String webPage = "https://www.sahibinden.com/";
    private static final String webDriverLocation = "webdriver/geckodriver.exe";
    private static final String webDriverLocationMac = "webdriver/geckodriver";
    private static String userAgent;
    private static int imageUpdateSpeed;
    private static int searchSpeed;
    private static FirefoxDriver driver;
    private static String htmlInformation;
    private static String osName = null;

    //Singleton class ;
    private Firefox() {
    }

    public static void setImageUpdateSpeed(int imageUpdateSpeed) {
        Firefox.imageUpdateSpeed = imageUpdateSpeed;
    }

    public static void setSearchUpdateSpeed(int searchSpeed) {
        Firefox.searchSpeed = searchSpeed;
    }

    public static String getUserAgent() {
        return userAgent;
    }

    public static void setUserAgent(String userAgent) {
        Firefox.userAgent = userAgent;
    }

    public static String getHtmlInformation() {
        return htmlInformation;
    }

    private static void waitForPageLoaded() {
        ExpectedCondition<Boolean> expectation = driver -> ((JavascriptExecutor) Objects.requireNonNull(driver)).executeScript("return document.readyState").toString().equals("complete");
        try {
            Thread.sleep(searchSpeed);
            System.out.println("Opening page with search delay: " + searchSpeed);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(expectation);
        } catch (Throwable error) {
            System.out.println("Request error");
        }
    }

    public static void updateUrl(String url, boolean wait) {
        updateUrl(url, wait, 0);
    }

    public static void updateUrl(String url, int time) {
        updateUrl(url, false, time);
    }


    private static void updateUrl(String url, boolean wait, int timeMs) {
        try {
            //Driver içine link alındı.
            driver.get(url);
            //Sayfa yüklenmesi beklendi.
            if (wait) {
                waitForPageLoaded();
            } else {
                try {
                    Thread.sleep(timeMs);
                } catch (InterruptedException e) {
                    System.out.println("Error on wait.");
                    e.printStackTrace();
                }
            }
            if (driver != null) {
                System.out.println(driver);
                htmlInformation = driver.getPageSource();
            } else {
                System.out.println("Link okunamadı.\n");
            }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createFirefox(String url, boolean outScreenFirefox, boolean hiddenScreen) {
        if (isWindows()) {
            System.setProperty("webdriver.gecko.driver", webDriverLocation);
        } else {
            System.setProperty("webdriver.gecko.driver", webDriverLocationMac);
        }
        // Sistem yapısı webDriver incelenerek oluşturuldu.
        FirefoxOptions options = new FirefoxOptions();
        //Seçenekler belirlendi.
        options.addPreference("general.useragent.override", userAgent);
        options.addPreference("javascript.enabled", true);
        //Eğer seçiliyse headless mode aktifletirilerek arka planda çalıştırıldı.
        if (hiddenScreen)
            options.addArguments("--headless");

        //Oluşturulan seçeneklere göre web driver oluşturuldu.
        driver = new FirefoxDriver(options);

        //Eğer seçiliyse webdriver ekrandan çıkartıldı.
        if (outScreenFirefox) {
            driver.manage().window().setPosition(new Point(2000, 2000));
        }
        updateUrl(url, true);
    }

    private static String getOsName() {
        if (osName == null) {
            osName = System.getProperty("os.name");
        }
        return osName;
    }

    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    public static void destroyFirefox() {
        try {
            if (isOperates()) {
                driver.close();
                driver.quit();
            }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean isOperates() {
        if (driver == null) {
            return false;
        }
        boolean operation = true;
        try {
            driver.getWindowHandles();
        } catch (WebDriverException | NullPointerException e) {
            operation = false;
            System.out.println("No driver found");
        }
        return operation;
    }

    @FXML
    public static Image LoadImageFromClassAndSrcInfo(String url, int width, int height) {
        Firefox.updateUrl(url, imageUpdateSpeed);
        JavascriptExecutor js = driver;
        String base64string = (String) js.executeScript("var c = document.createElement('canvas');"
                + " var ctx = c.getContext('2d');"
                + "var img = document.getElementsByTagName('img')[0];"
                + "c.height=img.naturalHeight;"
                + "c.width=img.naturalWidth;"
                + "ctx.drawImage(img, 0, 0,img.naturalWidth, img.naturalHeight);"
                + "var base64String = c.toDataURL();"
                + "return base64String;");
        String[] base64Array = base64string.split(",");

        String base64 = base64Array[base64Array.length - 1];
        byte[] img = Base64.getDecoder().decode(base64);
        return new Image(new ByteArrayInputStream(img), width, height, false, false);
    }

    public static String getWebPage() {
        return webPage;
    }
}
