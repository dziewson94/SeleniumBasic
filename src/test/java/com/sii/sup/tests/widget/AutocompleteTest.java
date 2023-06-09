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
import java.util.List;

import static com.sii.sup.staticvalues.StaticValues.Autocomplete.SEARCH_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
@Execution(ExecutionMode.CONCURRENT)

class AutocompleteTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(AutocompleteTest.class);

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/autocomplete.php"})
    void autocomplete(String url) {
        propertyHelper = TestHelper.getPropertyHelper(this.getClass().getSimpleName());
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

        WebElement randomOption = (WebElement) TestHelper.getRandomListElement(optionsList);
        randomOption.click();
        searchBox.click();
        assertThat(randomOption.getAttribute("textContent")).isEqualTo(searchBox.getAttribute(Attributes.VALUE.getValue()));
    }
}
