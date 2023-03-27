package com.sii.sup.widget;

import com.sii.sup.base.Attributes;
import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import com.sii.sup.helper.PropertyHelper;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

import static com.sii.sup.staticvalues.StaticValues.Autocomplete.SEARCH_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;

public class Autocomplete extends TestBase {
    private static Logger logger = LoggerFactory.getLogger(AccordionTest.class.getSimpleName());

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/autocomplete.php"})
    public void autocomplete(String url) {
        propertyHelper = new PropertyHelper(this.getClass().getSimpleName());
        pageHelper.init(url);
        WebElement searchBox = pageHelper.findElementById(getStringProperty(SEARCH_PROPERTY));
        searchBox.sendKeys("a");
        WebDriverWait wait = new WebDriverWait(pageHelper.getWebDriver(), Duration.ofSeconds(10));
        By autocompleteOptions = By.xpath("//ul[@id='ui-id-1']/li");
        wait.until(ExpectedConditions.visibilityOfElementLocated(autocompleteOptions));

        List<WebElement> optionsList = pageHelper.getWebDriver().findElements(autocompleteOptions);
        System.out.println("Autocomplete options:");
        for (WebElement option : optionsList) {
            logger.info("Found option:" + option.getText());
        }

        WebElement randomOption = (WebElement) Helper.getRandomListElement(optionsList);
        randomOption.click();
        searchBox.click();
        assertThat(randomOption.getAttribute("textContent")).isEqualTo(searchBox.getAttribute(Attributes.VALUE.getValue()));
    }
}
