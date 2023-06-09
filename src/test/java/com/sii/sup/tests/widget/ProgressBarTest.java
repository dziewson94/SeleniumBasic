package com.sii.sup.tests.widget;

import com.sii.sup.tests.base.Attributes;
import com.sii.sup.tests.base.TestBase;
import com.sii.sup.helper.TestHelper;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static com.sii.sup.staticvalues.StaticValues.Progressbar.*;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)
class ProgressBarTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(ProgressBarTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/progressbar.php"})
    void progressbarOne(String url) {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        pageHelper.init(url);
        WebElement progressBar = pageHelper.findElementById(getStringProperty(PROGRESSBAR_ID_PROPERTY));
        WebElement progressLabel = progressBar.findElement(By.className(getStringProperty(PROGRESSBAR_LABEL_PROPERTY)));
        logger.info("Waiting for progress  bar");
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(30)).
                until(ExpectedConditions.textToBePresentInElement(progressLabel, getStringProperty(PROGRESSBAR_LABEL_COMPLETE__PROPERTY)));
        assertThat(progressLabel.getText()).isEqualTo(getStringProperty(PROGRESSBAR_LABEL_COMPLETE__PROPERTY));

    }

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/progressbar.php"})
    void progressbarTwo(String url) {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
        pageHelper.init(url);
        WebElement progressBar = pageHelper.findElementById(getStringProperty(PROGRESSBAR_ID_PROPERTY));
        WebElement progressLabel = progressBar.findElement(By.className(getStringProperty(PROGRESSBAR_LABEL_PROPERTY)));
        WebElement progressLabelDiv = progressBar.findElement(By.className(getStringProperty(PROGRESSBAR_DIV_LABEL_COMPLETE__PROPERTY)));
        new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(30)).
                until(ExpectedConditions.attributeContains(progressLabelDiv, Attributes.CLASS.getValue(), getStringProperty(PROGRESSBAR_CLASS_COMPLETE__PROPERTY)));
        assertThat(progressLabel.getText()).isEqualTo(getStringProperty(PROGRESSBAR_LABEL_COMPLETE__PROPERTY));

    }
}
