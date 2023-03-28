package com.sii.sup.basic;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.PropertyHelper;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.sii.sup.staticvalues.StaticValues.AlertTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class AlertTest extends TestBase {
    private final Logger logger = new LoggerContext().getLogger(AlertTest.class);

    public void initProperties() {
        propertyHelper = TestHelper.getPropertyHelper(AlertTest.class.getSimpleName());
        logger.info("Alert test properties loaded");

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    void simpleAlertPopUp(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement simpleAlertWebElement = pageHelper.findElementById(getStringProperty(SIMPLE_ALERT_PROPERTY));
        logger.info("Clicking on " + getStringProperty(SIMPLE_ALERT_PROPERTY));
        simpleAlertWebElement.click();
        Alert alert = pageHelper.getWebDriver().switchTo().alert();
        alert.accept();
        String pageSource = pageHelper.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(getStringProperty(SIMPLE_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(SIMPLE_ALERT_SUCCESS_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    void promptAlertBox(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement promptAlertButton = pageHelper.findElementById(getStringProperty(PROMPT_ALERT_PROPERTY));
        logger.info("Clicking on " + getStringProperty(PROMPT_ALERT_PROPERTY));
        promptAlertButton.click();
        Alert alert = pageHelper.getWebDriver().switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        logger.info(String.format("Typing %s in alert", getStringProperty(PROMPT_ALERT_TEXT_PROPERTY)));
        alert.sendKeys(getStringProperty(PROMPT_ALERT_TEXT_PROPERTY));
        alert.accept();
        String pageSource = pageHelper.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(getStringProperty(PROMPT_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(PROMPT_ALERT_SUCCESS_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    void confirmAlertBox(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement alertButton = pageHelper.findElementById(getStringProperty(CONFIRM_ALERT_PROPERTY));
        logger.info("Clicking on " + getStringProperty(CONFIRM_ALERT_PROPERTY));
        alertButton.click();
        Alert alert = pageHelper.getWebDriver().switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        alert.accept();
        String pageSource = pageHelper.getWebDriver().getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(getStringProperty(CONFIRM_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(CONFIRM_ALERT_SUCCESS_PROPERTY));
        logger.info("Clicking on " + getStringProperty(CONFIRM_ALERT_PROPERTY));
        alertButton.click();
        alert = pageHelper.getWebDriver().switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        alert.dismiss();
        pageSource = pageHelper.getPageSource();
        logger.info("Is fail message on screen: " + pageSource.contains(getStringProperty(CONFIRM_ALERT_FAIL_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(CONFIRM_ALERT_FAIL_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    void delayedAlertPopUp(String url) {
        initProperties();
        pageHelper.init(url);
        WebElement delayedAlert = pageHelper.findElementById(getStringProperty(DELAYED_ALERT_PROPERTY));
        logger.info("Clicking on " + getStringProperty(DELAYED_ALERT_PROPERTY));
        delayedAlert.click();
        WebDriverWait wait = new WebDriverWait(pageHelper.getWebDriver(),
                Duration.ofSeconds(20));
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        String pageSource = pageHelper.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(getStringProperty(SIMPLE_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(getStringProperty(SIMPLE_ALERT_SUCCESS_PROPERTY));
    }
}
