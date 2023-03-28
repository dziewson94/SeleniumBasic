package com.sii.sup.basic;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.FileHelper;
import com.sii.sup.helper.PropertyHelper;
import com.sii.sup.helper.TestHelper;
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
    private final Logger logger = new LoggerContext().getLogger(TableTest.class);

    private void initProperties() {
        this.propertyHelper = TestHelper.getPropertyHelper(IframeTest.class.getSimpleName());
        logger.info("Iframe test properties loaded");
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/iframes.php"})
    void iframeTest(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement firstIframeWebElement = pageHelper.getWebDriver().findElement(By.name(getStringProperty(FIRST_IFRAME_PROPERTY)));
        pageHelper.getWebDriver().switchTo().frame(firstIframeWebElement);
        assertThat(firstIframeWebElement).isNotNull();
    }

}
