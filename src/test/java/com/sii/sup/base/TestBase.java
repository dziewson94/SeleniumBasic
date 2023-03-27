package com.sii.sup.base;

import com.sii.sup.helper.Helper;
import com.sii.sup.helper.PageHelper;
import com.sii.sup.helper.PropertyHelper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TestBase {
    public static final String REMOTE_ALLOW_ORIGINS = "--remote-allow-origins=*";
    public static final String DOWNLOAD_PROMPT_FOR_DOWNLOAD = "download.prompt_for_download";
    public static final String DOWNLOAD_DEFAULT_DIRECTORY = "download.default_directory";
    public static final String PREFS = "prefs";
    private static final Logger logger = LoggerFactory.getLogger(TestBase.class.getSimpleName());
    protected PageHelper pageHelper;
    protected PropertyHelper propertyHelper;
    private WebDriver webDriver;

    @BeforeAll
    public static void setupAll() {
        logger.info("Web driver manager initialization");
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setup() {
        logger.info("Initializing chrome webdriver");
        ChromeOptions options = new ChromeOptions();
        logger.info(
                String.format("Adding options:%n%s%n%s:false%n%s:%s",
                        REMOTE_ALLOW_ORIGINS, DOWNLOAD_PROMPT_FOR_DOWNLOAD, DOWNLOAD_DEFAULT_DIRECTORY, Helper.getDownloadDirPath()));
        options.addArguments(REMOTE_ALLOW_ORIGINS);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put(DOWNLOAD_PROMPT_FOR_DOWNLOAD, false);
        prefs.put(DOWNLOAD_DEFAULT_DIRECTORY, Helper.getDownloadDirPath());
        options.setExperimentalOption(PREFS, prefs);
        webDriver = new ChromeDriver(options);
        pageHelper = new PageHelper(webDriver);
    }


    @AfterEach
    public void tearDown() {
        logger.info("Cleaning up environment");
        webDriver.close();
    }
    protected String getStringProperty(String property) {
        return this.propertyHelper.get(property);
    }


    protected int getIntProperty(String property) {
        return this.propertyHelper.getInt(property);
    }
}
