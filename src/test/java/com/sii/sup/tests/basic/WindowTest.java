package com.sii.sup.tests.basic;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Set;

import static com.sii.sup.staticvalues.StaticValues.WindowTest.*;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class WindowTest extends TestBase {
    private final Logger logger = new LoggerContext().getLogger(WindowTest.class.getSimpleName());

    private void initProperties() {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        logger.info("Window test properties loaded");
    }
    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/windows-tabs.php"})
    void testNewBrowserTab(String url) {
        initProperties();
        pageHelper.init(url);
        String mainWindowHandle = pageHelper.getWebDriver().getWindowHandle();
        WebElement newBrowserTab = pageHelper.findElementById(getStringProperty(NEW_BROWSER_TAB_PROPERTY));
        logger.info("Click on " + getStringProperty(NEW_BROWSER_TAB_PROPERTY));
        newBrowserTab.click();
        Set<String> allWindowHandles = pageHelper.getWebDriver().getWindowHandles();
        String newBrowserTabHandle = pageHelper.getNewestWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newBrowserTabHandle);
        pageHelper.getWebDriver().switchTo().window(newBrowserTabHandle);
        TableTest tableTest = new TableTest();
        tableTest.setup();
        tableTest.tableTest(pageHelper.getWebDriver().getCurrentUrl());
    }
    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/windows-tabs.php"})
    void testNewMessageWindow(String url) {
        initProperties();
        pageHelper.init(url);
        String mainWindowHandle = pageHelper.getWebDriver().getWindowHandle();
        WebElement newMessageWindow = pageHelper.findElementById(getStringProperty(NEW_MESSAGE_WINDOW_PROPERTY));
        logger.info("Click on " + getStringProperty(NEW_MESSAGE_WINDOW_PROPERTY));
        newMessageWindow.click();
        Set<String> allWindowHandles = pageHelper.getWebDriver().getWindowHandles();
        String newMessageWindowHandle = pageHelper.getNewestWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newMessageWindowHandle);
        pageHelper.getWebDriver().switchTo().window(newMessageWindowHandle);
        WebElement body = pageHelper.getWebDriver().findElement(By.tagName(Attributes.BODY.getValue()));
        String bodyOutput = body.getText();
        logger.info("Text from body" + bodyOutput);
        pageHelper.getWebDriver().close();
        pageHelper.getWebDriver().switchTo().window(mainWindowHandle);
        assertThat(bodyOutput).isEqualTo(
                getStringProperty(NEW_MESSAGE_WINDOW_CONTENT_PROPERTY));

    }
    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/windows-tabs.php"})
    void testNewBrowserWindow(String url) {
        initProperties();
        pageHelper.init(url);
        String mainWindowHandle = pageHelper.getWebDriver().getWindowHandle();
        WebElement newBrowserButton = pageHelper.findElementById(getStringProperty(NEW_BROWSER_WINDOW_PROPERTY));
        logger.info("Click on " + getStringProperty(NEW_BROWSER_WINDOW_PROPERTY));
        newBrowserButton.click();
        logger.info("Current window handle " + mainWindowHandle);
        Set<String> allWindowHandles = pageHelper.getWebDriver().getWindowHandles();
        String newWindowHandle = pageHelper.getNewestWindowHandle(mainWindowHandle, allWindowHandles);
        logger.info("Switching to " + newWindowHandle);
        pageHelper.getWebDriver().switchTo().window(newWindowHandle);
        TableTest innerTableTest = new TableTest();
        innerTableTest.setup();
        innerTableTest.tableTest(pageHelper.getWebDriver().getCurrentUrl());
        logger.info("Switching to " + mainWindowHandle);
        pageHelper.getWebDriver().close();
        pageHelper.getWebDriver().switchTo().window(mainWindowHandle);

    }
}

