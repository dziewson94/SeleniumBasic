package com.sii.sup.basic;

import com.sii.sup.base.TestBase;
import com.sii.sup.basic.staticvalues.IframeTestStaticValues;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Properties;

import static com.sii.sup.basic.staticvalues.IframeTestStaticValues.FIRST_IFRAME_PROPERTY;
import static com.sii.sup.basic.staticvalues.IframeTestStaticValues.SECOND_IFRAME_PROPERTY;

@Execution(ExecutionMode.CONCURRENT)

public class IframeTest extends TestBase {
    private Properties properties;

    private void initProperties() {
        properties = Helper.readIframeProperties();
        logger.info("Iframe test properties loaded");

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/iframes.php"})
    public void iframeTest(String url) throws InterruptedException {
        initProperties();
        webDriver.get(url);
        WebElement firstIframeWebElement = webDriver.findElement(By.id(properties.getProperty(FIRST_IFRAME_PROPERTY)));
        WebElement secondIframeWebElement = webDriver.findElement(By.id(properties.getProperty(SECOND_IFRAME_PROPERTY)));
        webDriver.switchTo().frame(firstIframeWebElement);
        logger.info("Switched to first Iframe");
    }

}
