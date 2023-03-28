package com.sii.sup.tests.other;

import com.sii.sup.helper.TestHelper;
import com.sii.sup.tests.base.TestBase;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

import static com.sii.sup.staticvalues.StaticValues.HighSite.HIGH_SITE_OFFSET_PROPERTY;
import static com.sii.sup.staticvalues.StaticValues.HighSite.SCROLL_BUTTON_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

class HighSiteTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(HighSiteTest.class.getSimpleName());

    @ParameterizedTest
    @MethodSource("com.sii.sup.helper.DataSources#highSiteTestDataSource")
    void highSiteTest(String url, boolean javascript) {
        pageHelper.init(url);
        propertyHelper = TestHelper.getPropertyHelper(HighSiteTest.class.getSimpleName());
        scrollUntil(javascript);
        assertThat(pageHelper.findElementById(getStringProperty(SCROLL_BUTTON_PROPERTY)).isDisplayed()).isTrue();
        logger.info("");

    }

    private void scrollUntil(boolean javascript) {
        int i = 0;
        while (true) {
            WebElement scrollButton = null;
            try {
                scrollButton = pageHelper.findElementById(getStringProperty(SCROLL_BUTTON_PROPERTY));
            } catch (NoSuchElementException exception) {
                scroll(javascript);
                logger.info("Moving down by " + getIntProperty(HIGH_SITE_OFFSET_PROPERTY) + "px. Iteration:" + i);
            }
            if (scrollButton != null) {
                logger.info("Target found at " + scrollButton.getLocation().toString() + "!");
                break;
            }
            i++;
        }
        takeScreenShot();
    }

    private void scroll(boolean javascript) {
        if (!javascript) {
            Actions actions = new Actions(pageHelper.getWebDriver());
            actions.scrollByAmount(0, getIntProperty(HIGH_SITE_OFFSET_PROPERTY)).perform();
        } else {
            JavascriptExecutor js = (JavascriptExecutor) pageHelper.getWebDriver(); // Cast the driver to JavascriptExecutor
            js.executeScript(String.format("window.scrollBy(0, %d)", getIntProperty(HIGH_SITE_OFFSET_PROPERTY)));
        }
    }

    private void takeScreenShot() {
        // Cast the driver to TakesScreenshot
        logger.info("Taking screenshot");
        TakesScreenshot screenshot = (TakesScreenshot) pageHelper.getWebDriver();
        File file = screenshot.getScreenshotAs(OutputType.FILE);
        logger.info("Screenshot taken. Saving it in:" + TestHelper.getPathForScreenshot());
        if (new File(TestHelper.getPathForScreenshot()).exists()) {
            logger.info("File deleted:" + new File(TestHelper.getPathForScreenshot()).delete());
        }
        try {
            FileUtils.copyFile(file, new File(TestHelper.getPathForScreenshot()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertThat(new File(TestHelper.getPathForScreenshot())).exists();
        logger.info("File deleted:" + new File(TestHelper.getPathForScreenshot()).delete());


    }


}
