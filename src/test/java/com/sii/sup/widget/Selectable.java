package com.sii.sup.widget;

import com.sii.sup.base.TestBase;
import com.sii.sup.helper.Helper;
import com.sii.sup.helper.PropertyHelper;
import com.sii.sup.staticvalues.StaticValues;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

import static com.sii.sup.staticvalues.StaticValues.Selectable.*;

public class Selectable extends TestBase {

    private static final Logger logger = LoggerFactory.getLogger(Selectable.class);

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/selectmenu.php"})
    public void selectableTest(String url) {
        propertyHelper = new PropertyHelper(Selectable.class.getSimpleName());
        pageHelper.init(url);

        WebElement speedDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_SPEED_PROPERTY));
        speedDropdown.click();
        List<WebElement> speedOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='speed-menu']/li"));
        int randomSpeedIndex = (int) (Math.random() * speedOptions.size());
        speedOptions.get(randomSpeedIndex).click();

        WebElement fileDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_FILES__PROPERTY));
        fileDropdown.click();
        List<WebElement> fileOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='files-menu']//li"));
        fileOptions.stream().filter(op -> op.getText().equals(getStringProperty(SELECTABLE_FILENAME_PROPERTY))).findFirst().ifPresent(WebElement::click);

        WebElement numberDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_NUMBER_PROPERTY));
        numberDropdown.click();
        List<WebElement> numberOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='number-menu']/li"));
        int optionIndex = getIntProperty(SELECTABLE_NUM_INDEX_PROPERTY); // index of the option to select
        numberOptions.get(optionIndex).click();

        WebElement titleDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_SALUTATION_PROPERTY));
        titleDropdown.click();
        List<WebElement> titleOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='salutation-menu']/li"));
        int randomIndex = (int) (Math.random() * titleOptions.size());
        titleOptions.get(randomIndex).click();
    }


}
