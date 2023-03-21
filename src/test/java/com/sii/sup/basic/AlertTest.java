package com.sii.sup.basic;

import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Properties;

import static com.sii.sup.basic.utils.AlertTestStaticValues.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
public class AlertTest extends TestBase {
    private Properties properties;

    public void initData() {
        properties = Helper.readAlertProperties();
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    public void simpleAlertPopUp(String url) {
        initData();
        webDriver.get(url);
        WebElement simpleAlertWebElement = webDriver.findElement(By.id(properties.getProperty(SIMPLE_ALERT_PROPERTY)));
        logger.info("Clicking on " + properties.getProperty(SIMPLE_ALERT_PROPERTY));
        simpleAlertWebElement.click();
        Alert alert = webDriver.switchTo().alert();
        alert.accept();
        String pageSource = webDriver.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(properties.getProperty(SIMPLE_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(SIMPLE_ALERT_SUCCESS_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    public void promptAlertBox(String url) {
        initData();
        webDriver.get(url);
        WebElement promptAlertButton = webDriver.findElement(By.id(properties.getProperty(PROMPT_ALERT_PROPERTY)));
        logger.info("Clicking on " + properties.getProperty(PROMPT_ALERT_PROPERTY));
        promptAlertButton.click();
        Alert alert = webDriver.switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        logger.info(String.format("Typing %s in alert", properties.getProperty(PROMPT_ALERT_TEXT_PROPERTY)));
        // element.forEach(el-> System.out.println(el.getAttribute("id")));
        alert.sendKeys(properties.getProperty(PROMPT_ALERT_TEXT_PROPERTY));
        alert.accept();
        String pageSource = webDriver.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(properties.getProperty(PROMPT_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(PROMPT_ALERT_SUCCESS_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    public void confirmAlertBox(String url) {
        initData();
        webDriver.get(url);
        WebElement alertButton = webDriver.findElement(By.id(properties.getProperty(CONFIRM_ALERT_PROPERTY)));
        logger.info("Clicking on " + properties.getProperty(CONFIRM_ALERT_PROPERTY));
        alertButton.click();
        Alert alert = webDriver.switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        alert.accept();
        String pageSource = webDriver.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(properties.getProperty(CONFIRM_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(CONFIRM_ALERT_SUCCESS_PROPERTY));
        logger.info("Clicking on " + properties.getProperty(CONFIRM_ALERT_PROPERTY));
        alertButton.click();
        alert = webDriver.switchTo().alert();
        logger.info("Alert text:" + alert.getText());
        alert.dismiss();
        pageSource = webDriver.getPageSource();
        logger.info("Is fail message on screen: " + pageSource.contains(properties.getProperty(CONFIRM_ALERT_FAIL_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(CONFIRM_ALERT_FAIL_PROPERTY));
    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/alerts.php"})
    public void delayedAlertPopUp(String url) {
        initData();
        webDriver.get(url);
        WebElement delayedAlert = webDriver.findElement(By.id(properties.getProperty(DELAYED_ALERT_PROPERTY)));
        logger.info("Clicking on " + properties.getProperty(DELAYED_ALERT_PROPERTY));
        delayedAlert.click();
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.alertIsPresent()).accept();
        String pageSource = webDriver.getPageSource();
        logger.info("Is success message on screen: " + pageSource.contains(properties.getProperty(SIMPLE_ALERT_SUCCESS_PROPERTY)));
        assertThat(pageSource).contains(properties.getProperty(SIMPLE_ALERT_SUCCESS_PROPERTY));
    }
}
