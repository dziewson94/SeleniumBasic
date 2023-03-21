package com.sii.sup.basic;

import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Properties;

import static com.sii.sup.staticvalues.StaticValues.IframeTest.FIRST_IFRAME_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class IframeTest extends TestBase {
    private Properties properties;

    private void initProperties() {
        properties = Helper.readIframeProperties();
        logger.info("Iframe test properties loaded");

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/iframes.php"})
    void iframeTest(String url) {
        initProperties();
        webDriver.get(url);
        WebElement firstIframeWebElement = webDriver.findElement(By.name(properties.getProperty(FIRST_IFRAME_PROPERTY)));
        webDriver.switchTo().frame(firstIframeWebElement);
        assertThat(firstIframeWebElement).isNotNull();
    }

}
