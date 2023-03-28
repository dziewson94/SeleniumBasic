package com.sii.sup.tests.widget;

import com.sii.sup.helper.TestHelper;
import com.sii.sup.tests.base.TestBase;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.sii.sup.staticvalues.StaticValues.Selectable.*;
import static org.assertj.core.api.Assertions.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class SelectMenuTest extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(SelectMenuTest.class);
    private String expectedFileOption;

    @ParameterizedTest
    @ValueSource(strings = {"http://www.seleniumui.moderntester.pl/selectmenu.php"})
    void selectableTest(String url) {
        propertyHelper = TestHelper.getPropertyHelper(SelectMenuTest.class.getSimpleName());
        pageHelper.init(url);

        WebElement speedDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_SPEED_PROPERTY));
        speedDropdown.click();
        List<WebElement> speedOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='speed-menu']/li"));
        int randomSpeedIndex = (int) (Math.random() * speedOptions.size());
        String expectedSpeed = speedOptions.get(randomSpeedIndex).getText();
        speedOptions.get(randomSpeedIndex).click();
        logger.info("Random speed selected " + expectedSpeed);
        WebElement fileDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_FILES__PROPERTY));
        fileDropdown.click();
        List<WebElement> fileOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='files-menu']//li"));
        fileOptions.stream().filter(op -> op.getText().equals(getStringProperty(SELECTABLE_FILENAME_PROPERTY))).findFirst().ifPresent(el -> {
            expectedFileOption = el.getText();
            el.click();
        });
        logger.info("File option selected " + expectedFileOption);
        WebElement numberDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_NUMBER_PROPERTY));
        numberDropdown.click();

        List<WebElement> numberOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='number-menu']/li"));
        int optionIndex = getIntProperty(SELECTABLE_NUM_INDEX_PROPERTY); // index of the option to select
        String expectedNumberOption = numberOptions.get(optionIndex).getText();
        numberOptions.get(optionIndex).click();
        logger.info("Number option selected " + expectedNumberOption);
        WebElement titleDropdown = pageHelper.findElementById(getStringProperty(SELECTABLE_SALUTATION_PROPERTY));
        titleDropdown.click();
        List<WebElement> titleOptions = pageHelper.getWebDriver().findElements(By.xpath("//ul[@id='salutation-menu']/li"));
        int randomIndex = (int) (Math.random() * titleOptions.size());
        String expectedTitleOption = titleOptions.get(randomIndex).getText();
        titleOptions.get(randomIndex).click();
        logger.info("Title option selected  " + expectedTitleOption);
        assertThat(speedDropdown.getText()).isEqualTo(expectedSpeed);
        assertThat(fileDropdown.getText()).isEqualTo(expectedFileOption);
        assertThat(numberDropdown.getText()).isEqualTo(expectedNumberOption);
        assertThat(titleDropdown.getText()).isEqualTo(expectedTitleOption);


    }


}
