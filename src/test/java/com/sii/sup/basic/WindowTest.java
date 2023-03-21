package com.sii.sup.basic;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.staticvalues.StaticValues;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Properties;
import java.util.Set;

import static com.sii.sup.staticvalues.StaticValues.WindowTest.*;
import static org.assertj.core.api.Assertions.assertThat;

class WindowTest extends TestBase {
    private Properties properties;

    private static String getNewWindowHandle(String currentWindowHandle, Set<String> allWindowHandles) {
        return allWindowHandles.stream().filter(handle -> (!handle.equals(currentWindowHandle))).findFirst().orElse("");
    }

    private void initProperties() {
        properties = Helper.readWindowProperties();
        logger.info("Window test properties loaded");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/windows-tabs.php"})
    void windowTest(String url) {
        initProperties();
        webDriver.get(url);
        String mainWindowHandle = webDriver.getWindowHandle();
        testNewBrowserWindow(mainWindowHandle);
        testNewMessageWindow(mainWindowHandle);
        testNewBrowserTab(mainWindowHandle);
    }

    private void testNewBrowserTab(String mainWindowHandle) {
        WebElement newBrowserTab = webDriver.findElement(By.id(properties.getProperty(NEW_BROWSER_TAB_PROPERTY)));
        logger.info("Click on " + properties.getProperty(NEW_BROWSER_TAB_PROPERTY));
        newBrowserTab.click();
        Set<String> allWindowHandles = webDriver.getWindowHandles();
        String newBrowserTabHandle = getNewWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newBrowserTabHandle);
        webDriver.switchTo().window(newBrowserTabHandle);
        TableTest tableTest = new TableTest();
        tableTest.setup();
        tableTest.tableTest(webDriver.getCurrentUrl());
    }

    private void testNewMessageWindow(String mainWindowHandle) {
        WebElement newMessageWindow = webDriver.findElement(By.id(properties.getProperty(NEW_MESSAGE_WINDOW_PROPERTY)));
        logger.info("Click on " + properties.getProperty(NEW_MESSAGE_WINDOW_PROPERTY));
        newMessageWindow.click();
        Set<String> allWindowHandles = webDriver.getWindowHandles();
        String newMessageWindowHandle = getNewWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newMessageWindowHandle);
        webDriver.switchTo().window(newMessageWindowHandle);
        WebElement body = webDriver.findElement(By.tagName(Attributes.BODY.getValue()));
        logger.info("Text from body" + body.getText());
        assertThat(body.getText()).isEqualTo(
                properties.getProperty(StaticValues.WindowTest.NEW_MESSAGE_WINDOW_CONTENT_PROPERTY));
        webDriver.close();
        webDriver.switchTo().window(mainWindowHandle);
    }

    private void testNewBrowserWindow(String mainWindowHandle) {
        WebElement newBrowserButton = webDriver.findElement(By.id(properties.getProperty(NEW_BROWSER_WINDOW_PROPERTY)));
        logger.info("Click on " + properties.getProperty(NEW_BROWSER_WINDOW_PROPERTY));
        newBrowserButton.click();
        logger.info("Current window handle " + mainWindowHandle);
        Set<String> allWindowHandles = webDriver.getWindowHandles();
        String newWindowHandle = getNewWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newWindowHandle);
        webDriver.switchTo().window(newWindowHandle);
        TableTest innerTableTest = new TableTest();
        innerTableTest.setup();
        innerTableTest.tableTest(webDriver.getCurrentUrl());
        logger.info("Switching to " + mainWindowHandle);
        webDriver.close();
        webDriver.switchTo().window(mainWindowHandle);

    }

}

